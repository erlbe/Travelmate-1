/**
 * Created by Erlend on 11.10.2016.
 */

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
};