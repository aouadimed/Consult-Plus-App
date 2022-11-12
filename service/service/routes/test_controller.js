const { request } = require("express");
const express = require("express");
const router = express.Router();
const mongoose = require("mongoose");
const Test = mongoose.model("test")

router.post("/addTest", (req, res) => {
    const { test_status, test_name, user_id } = req.body;
    if (!test_status || !test_name || !user_id) {
      res.json({ error: "please add all the feilds" });
    }
        const test = new Test({
          test_status,  
          test_name,
          user_id,
        });
        test
          .save()
          .then((test) => {
            res.json({ message: "successfuly Added" });
          })
          .catch((err) => {
            console.log(err);
          });  
  });







  module.exports = router;