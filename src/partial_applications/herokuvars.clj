(ns partial-applications.herokuvars)

(def secrets {:memcache-server (System/getenv "MEMCACHE_SERVERS")
              :memcache-user (System/getenv "MEMCACHE_SERVERS")
              :memcache-pw (System/getenv "MEMCACHE_PASSWORD")
              :mongo-url (System/getenv "MONGOLAB_URI")
              :mongo-user (System/getenv "MONGOLAB_USER")})
