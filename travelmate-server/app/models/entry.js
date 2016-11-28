/**
 * Created by Erlend on 11.10.2016.
 */

var mongoose = require('mongoose');

var entrySchema = mongoose.Schema({
        _id: Number,
        _creator : { type: mongoose.Schema.Types.ObjectId, ref: 'User'},
        _category : { type: mongoose.Schema.Types.ObjectId, ref: 'Category'},
        title: String,
        content: String,
        image: String
    }
);

module.exports = mongoose.model('Entry', entrySchema);