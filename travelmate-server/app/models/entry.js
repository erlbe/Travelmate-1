/**
 * Created by Erlend on 11.10.2016.
 */

var mongoose = require('mongoose');

var entrySchema = mongoose.Schema({
        _creator : { type: mongoose.Schema.Types.ObjectId, ref: 'User'},
        title: String,
        text: String
    }
);

module.exports = mongoose.model('Entry', entrySchema);