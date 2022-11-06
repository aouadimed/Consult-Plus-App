const { request } = require('express')
const express = require('express')
const app = express()
const PORT = 5000
const mongoose = require('mongoose')
const {MONGOURI} = require('./keys')


mongoose.connect(MONGOURI)
mongoose.connection.on('connected', ()=>{
	console.log("conncted to mongo")
})
mongoose.connection.on('error', (err) => {
	console.log("error connecting",err)
})

require('./models/user')
 
app.use(express.json())
app.use(require('./routes/auth'))





app.listen(PORT, ()=> {
	console.log("Server is running on port" ,PORT)
})