const mongoose = require('mongoose')

const usershema = new mongoose.Schema(
    {
      
        email: {
            type: String,
            required:true
        },
        name:{
            type : String,
            required : true
        },
        password: {
            type: String,
            required: true
        }
        
    })
mongoose.model("User", usershema)