#!/usr/bin/env node

var http = require('http');
var fs = require('fs');
var optimist = require('optimist');
var argv = optimist
            .usage('Return a cryptic aphorism.\nUsage: $0')
            .options({
                 help: {
                    alias: 'h',
                    boolean: true,
                    description: 'Display this usage information.'
                 },
                 update: {
                     alias: 'u',
                     boolean: true,
                     description: 'Download the latest Partial Applications strategies.'
                 },
                 number: {
                     alias: 'n',
                     description: 'Return the given strategy. Takes a positive decimal or hex integer.'
                 }})
                 .check(function(argv) {
                     if (argv.n !== undefined) {
                         if ((typeof(argv.n) !== 'number') || (argv.n <= 0)) {
                             throw "-n should specify a positive number.";
                         }
                     } })
                .argv;

function update() {
    var options = {hostname: "localhost", port: "3000", path: "/json/all/"};
    http.get(options, function(res) {
        res.on('data', function(chunk) {
            fs.writeFile('strategies.json', chunk, function (err) {
                if (err) { 
                    throw err;
                } else {
                    console.log('Updated strategies.json');
                }
        });
    });
 });
}

function getStrategy(id) {
    fs.readFile('strategies.json', function (err, data) {
       if (err) { 
           throw err; 
       } else {
           var strategies = JSON.parse(data);
           var strategy, hex_string;
           var n = strategies.allStrategies.length;
           if (id === null || id === undefined) {
               var i = Math.floor(Math.random() * n);
               strategy = strategies.allStrategies[i].strategy;
               hex_string = "0x" + i.toString(16);
               console.log(hex_string + ": " + strategy);
           } else {
               if (id > n) {
                   console.log("Sorry, but there are only " + n + " strategies. Here's a random one:")
                   getStrategy(null);
               } else {
                   var i = id - 1;
                   strategy = strategies.allStrategies[i].strategy;
                   hex_string = "0x" + id.toString(16);
                   console.log(hex_string + ": " + strategy);
               }
           }
       }
    });
}

if (argv.h === true) {
    optimist.showHelp(console.log);
}

if (argv.u === true) {
    update();
}

if ((argv.n === undefined) || (argv.n > 0)) {
    if ((!argv.h) && (!argv.u)) {
        getStrategy(argv.n);
    }
}
