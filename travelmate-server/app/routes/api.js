/**
 * Created by Erlend on 11.10.2016.
 */

var Entry = require('../models/entry');
var User = require('../models/user');

module.exports = function(router, passport) {

    router.get('/login', function(req, res){
        res.send(req.user);
    });

    // Login from app. Return the user
    router.post('/login', function(req, res, next) {
        console.log(req.body);
        passport.authenticate('local-login', function(err, user, info){
            if(err){
                return next(err);
            }
            if (!user){
                return res.send(401, {error: 'message'});
            }

            req.login(user, function(err){
                if(err){
                    return next(err);
                }
                return res.send(req.user);
            });
        })(req, res, next);
    });

    // process the signup form
    router.post('/signup', passport.authenticate('local-signup'), function(req, res){
        res.send(req.user);
    });

    // GET all entries
    router.get('/entries/:userId', function(req, res, next){
        Entry.find({ _creator: req.params.userId }, function(err, entries){
            res.send(entries);
        })
    });

    // POST a new entry
    router.post('/entry', function(req, res, next){
        var entry = new Entry();
        console.log(req.body);

        entry._id = generateId();
        entry.title = req.body.title;
        entry.content = req.body.content;
        entry._creator = req.body.userId;
        entry.image = req.body.image;
        entry.category = req.body.category;

        // TODO: Add category

        // save the entry
        entry.save(function(err) {
            if (err) res.send(err);
            res.json(entry);
        });
    });

    // DELETE an entry
    router.delete('/entry/:id', function(req, res, next){
        Entry.findOneAndRemove({_id: req.params.id}, function(err, entry){
            if (err)
                res.send(err);

            res.send(entry);
            //res.json({ message: 'Entry deleted!' })
        })
    });

    // PUT an entry
    router.put('/entry/:id', function(req, res, next){
        Entry.findById(req.params.id, function(err, entry){
            if (err)
                res.send(err);

            entry.title = req.body.title;
            entry.content = req.body.content;
            entry._creator = req.body.userId;
            entry.image = req.body.image;
            entry.category = req.body.category;


            // save the entry
            entry.save(function(err) {
                if (err) res.send(err);
                res.json({ message: 'Entry updated!' });
            });
        })
    })

    router.get('entry/:id', function(req, res, next){
        Entry.findById(req.params.id, function(err, entry){
            if (err)
                res.send(err);
            res.send(entry);
        })
    })
};

var generateId = function () {
    return Math.floor(Math.random() * (999999999))
};