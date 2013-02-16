(ns partial-applications.memcachehacks
  (:import [net.spy.memcached.auth AuthDescriptor PlainCallbackHandler]
            net.spy.memcached.transcoders.Transcoder
            net.spy.memcached.transcoders.SerializingTranscoder
           [net.spy.memcached MemcachedClient 
                              ConnectionFactory 
                              BinaryConnectionFactory
                              ConnectionFactoryBuilder
                              ConnectionFactoryBuilder$Protocol
                              FailureMode
                              AddrUtil]
           [java.io ByteArrayOutputStream ByteArrayInputStream File]
           [java.net InetSocketAddress]))

(defn- servers
  [^String server-list]
  (AddrUtil/getAddresses server-list))

(defprotocol AsFailureMode
  (^FailureMode to-failure-mode [input]))

(extend-protocol AsFailureMode
  FailureMode
  (to-failure-mode [input]
    input)

  String
  (to-failure-mode [input]
    (case input
      "redistribute" FailureMode/Redistribute
      "retry"        FailureMode/Retry
      "cancel"       FailureMode/Cancel))

  clojure.lang.Named
  (to-failure-mode [input]
    (to-failure-mode (name input))))

(defn- ^ConnectionFactory customize-factory
  [^ConnectionFactory cf {:keys [failure-mode transcoder auth-descriptor]}]
  (let [;; Houston, we have a *FactoryFactory here!
        cfb (ConnectionFactoryBuilder. cf)]
    (when failure-mode
      (.setFailureMode cfb (to-failure-mode failure-mode)))
    (when transcoder
      (.setTranscoder cfb transcoder))
    (when auth-descriptor
      (.setAuthDescriptor cfb auth-descriptor))
    (.build cfb)))

(defn ^ConnectionFactory bin-connection-factory
  [& {:as opts}]
  (customize-factory (BinaryConnectionFactory.) opts))

(defn bin-connection
  "Returns a new binary protocol client that will use the provided list of servers."
  ([^String server-list]
     (MemcachedClient. (BinaryConnectionFactory.) (servers server-list)))
  ([^String server-list ^BinaryConnectionFactory cf]
     (MemcachedClient. cf (servers server-list)))
  ([^String server-list ^String username ^String password]
     (let [ad (AuthDescriptor/typical username password)]
       (MemcachedClient. (bin-connection-factory :auth-descriptor ad) (servers server-list)))))

(defn connect [server user pw]
  (let [auth (AuthDescriptor/typical user pw)
        tmc-client (bin-connection server
                                   (bin-connection-factory
                                     :failure-mode :redistribute 
                                     :auth-descriptor auth))]
    tmc-client))

;; chrismoos, Github

(defn make-memcached-client [server user pw]
  (let [servers (map (fn [^String s] 
        (let [sp (.split s ":")
              ^String host (aget sp 0)
              ^Integer port (if (> (alength sp) 1) (Integer/parseInt (aget sp 1)) 11211)]
          (InetSocketAddress. host port)))
        (.split server ";"))
        username user
        password pw
        callback-handler (PlainCallbackHandler. username password)
        auth-descriptor (AuthDescriptor. (into-array ["PLAIN"]) callback-handler)
        builder (-> (ConnectionFactoryBuilder.)
                  (.setAuthDescriptor auth-descriptor)
                  (.setProtocol (ConnectionFactoryBuilder$Protocol/BINARY)))
        conn-factory (.build builder)] 
    (let [^MemcachedClient client (MemcachedClient. conn-factory servers)]
      client)))

(defn new-connect [server user pw]
  (make-memcached-client server user pw))
