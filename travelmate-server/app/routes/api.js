/**
 * Created by Erlend on 11.10.2016.
 */

module.exports = function(router, passport) {

    router.get('/', function(req, res) {
        res.send("hellu"); // load the index.ejs file
    });
};