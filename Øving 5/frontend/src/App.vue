<template>
  <h1>Kjør Python-kode</h1>
  <textarea v-model="input" name="input" id="input" cols="50" rows="15" placeholder="Skriv din kode her"></textarea>
  <br>
  <button @click="run()">Kjør</button>
  <p>Output:</p>
  <textarea v-model="output" name="output" id="output" cols="50" rows="10" disabled></textarea>

</template>

<script setup>

  import {ref} from 'vue'
  import axios from 'axios'

  var input = ""
  var output = ref("")
      
  function run() {
    console.log(input)

    axios.get('http://localhost:8080/', {
        params: {
          input: this.input
        }
      })
        .then(response => {
            this.output = response.data
            console.log(response.data)
        })
        .catch(error => {
            console.log(error)
        })
  }

  function hello() {
    console.log(input)

    axios.get('http://localhost:8080/hello')
        .then(response => {
            this.output = response.data
            console.log(response.data)
        })
        .catch(error => {
            console.log(error)
        })
  }

</script>

<style>
body {
  background-color: #dcfff3ba;
  font-family: Verdana, Geneva, Tahoma, sans-serif;
  text-align: center;
}
textarea {
  resize: none;
}
button {
  border-radius: 5px;
}
button:hover {
  background-color: #5b5b5b;
  color: #FFFFFF;
  cursor: pointer;
}
</style>
