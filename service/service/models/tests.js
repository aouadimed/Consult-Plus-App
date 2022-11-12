const mongoose = require('mongoose')


const Testshema = new mongoose.Schema(
    {
      
        test_status: {
            type: Number,
            enum: [1, 2, 3],
            default : 1
        },
        test_name:{
            type : String,
            maxlength : 40 ,
            required : true
        },
    user_id: {
            type: mongoose.Schema.Types.ObjectId,
            ref: "User",
            required: true
          }
    
        
    })
mongoose.model("Test", Testshema)