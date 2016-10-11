/**
 * Created by Erlend on 11.10.2016.
 */

var mongoose = require('mongoose');

var categorySchema = mongoose.Schema({
        _creator : { type: mongoose.Schema.Types.ObjectId, ref: 'User'},
        title: String
    }
);

module.exports = mongoose.model('Category', categorySchema);