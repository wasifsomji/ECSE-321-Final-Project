<template>
  <div class="customerAccount">
    <div class="background">
      <div class="navbar-container">
        <nav class="navbar navbar-expand-lg navbar-light transparent-background">
          <a class="navbar-brand" href="#">
            <img src="../../assets/marwaniottNoBG.png" alt="Your Logo" height="60">
          </a>
          <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
          </button>
          <div class="collapse navbar-collapse justify-content-end" id="navbarNav">
            <ul class="navbar-nav">
              <li class="nav-item">
                <a class="nav-link clickable-text" @click="Home">Home</a>
              </li>
              <li class="nav-item">
                <a class="nav-link clickable-text" @click="Reservations">Reservations</a>
              </li>
              <li class="nav-item active">
                <a class="nav-link" href="#">Account<span class="sr-only">(current)</span></a>
              </li>
              <li class="nav-item">
                <a class="nav-link clickable-text" @click="LogOut">Log Out</a>
              </li>
            </ul>
          </div>
        </nav>
      </div>

      <div class="profile-box">
        <div class="container rounded bg-white mt-5 mb-5 account-box shadow">
        <div class="row">
          <div class="col-md-3 border-right">
            <div class="d-flex flex-column align-items-center justify-content-center text-center p-3 py-5 image-pos">
              <img class="rounded-circle" width="100%" src="../../assets/anonymousicon.png" alt="Profile Photo">
            </div>
          </div>
            <div class="col-md-9">
              <div class="p-3 py-5">
                <div class="d-flex justify-content-between align-items-center mb-3">
                  <h4 class="text-right" style="font-family: 'Montserrat', sans-serif; color: #888; letter-spacing: 3px">ACCOUNT</h4>
                </div>

                <div class="image-pos">
                  <div class="row mt-3">
                    <div class="col-md-6">
                      <label class="labels">Name</label>
                      <input class="form-control" id="name" v-model="name" readonly>
                    </div>
                    <div class="col-md-6">
                      <label class="labels">Email</label>
                      <input class="form-control" id="email" :value="email" readonly>
                    </div>
                    <div class="col-md-6">
                      <label class="labels">Password</label>
                      <input class="form-control" id="password" type="password" v-model="password" readonly>
                    </div>
                    <div class="col-md-6">
                      <label class="labels">Address</label>
                      <input class="form-control" id="address" v-model="address" readonly>
                    </div>
                    <div class="col-md-12">
                      <label class="labels">Date of Birth</label>
                      <input class="form-control" id="dob" type="date" v-model="dob" readonly>
                    </div>
                  </div>
                </div>
                <div class="mt-5 text-center">
                  <div class="row">
                    <div class="col-md-6">
                      <button @click="editInfo" type="button" class="btn btn-primary btn-block mb-2 editbutton">Edit Profile</button>
                    </div>
                    <div class="col-md-6">
                      <button @click="saveInfo" type="button" class="btn btn-primary btn-block mb-2 savebutton">Save Profile</button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
var config = require('../../../config')
var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort
var axiosClient = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

export default {
  name: 'CustomerAccount',
  data() {
    return {
      password: '',
      name: '',
      address: '',
      dob: '',
      errorMsg: '',
      accountNumber: 0,
      email: "",
    };
  },
  // mounted(){
  //   this.email = this.$route.params.param1
  // },
  created(){
    this.email = this.$route.params.param1

    axiosClient.get("/customer?email=" + this.email)
      .then((response) => {
        this.name = response.data.name;
        this.accountNumber = response.data.accountNumber;

        axiosClient.get("/account/?accountNumber=" + this.accountNumber)
          .then((response) => {
            this.address = response.data.address;
            this.dob = response.data.dob;
            this.password = response.data.password;
          })
          .catch((err) => {
            this.errorMsg = `Failure: ${err.response.data}`
            alert(this.errorMsg)
          })
      })
      .catch((err) => {
        this.errorMsg = `Failure: ${err.response.data}`
        alert(this.errorMsg)
      })
  },
  methods: {
    async saveInfo(){
      this.name = document.getElementById("name").value;
      this.address = document.getElementById("address").value;
      this.password = document.getElementById("password").value;
      this.dob = document.getElementById("dob").value;

      axiosClient.get("/customer?email=" + this.email)
        .then((response) => {
          this.accountNumber = response.data.accountNumber;
        })
        .catch((err) => {
          this.errorMsg = `Failure: ${err.response.data}`
          alert(this.errorMsg)
        })

      const account_request = {password: this.password, address: this.address, dob: this.dob};
      axiosClient.put("/account/" + this.accountNumber, account_request)
        .then((response) => {
          this.password = response.data.password;
          this.address = response.data.address;
          this.dob = response.data.dob;

          const customer_request = {name: this.name, email: this.email, accountNumber: this.accountNumber}
          axiosClient.put("/customer/update", customer_request)
            .then((response) => {
              this.name = response.data.name;
              alert("Account successfully updated.")

              document.getElementById('name').setAttribute('readonly', 'true');
              document.getElementById('password').setAttribute('readonly', 'true');
              document.getElementById('address').setAttribute('readonly', 'true');
              document.getElementById('dob').setAttribute('readonly', 'true');

            })
            .catch((err) => {
              this.errorMsg = `Failure: ${err.response.data}`
              alert(this.errorMsg)
            })
        })
        .catch((err) => {
          this.errorMsg = `Failure: ${err.response.data}`
          alert(this.errorMsg)
        })
    },
    async editInfo(){
      document.getElementById('name').removeAttribute('readonly');
      document.getElementById('password').removeAttribute('readonly');
      document.getElementById('address').removeAttribute('readonly');
      document.getElementById('dob').removeAttribute('readonly');
    },
    async Home() {
      await this.$router.push({path: '/CustomerHome/' + this.email})
    },
    async LogOut() {
      await this.$router.push({name: 'Home'})
    },
    async Reservations() {
      await this.$router.push({path: '/customer/reservation/' + this.email})
    },
  }
};

</script>

<style>
.background {
  width: 100%;
  height: 100%;
  position: absolute;
  background: url('../../assets/hotelView.png') center center no-repeat;
  background-size: cover;
}

.customerAccount {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.account-box {
  background-color: rgba(255, 255, 255, 0.8);
  padding: 20px;
  border-radius: 10px;
  top: 50%;
}

.image-pos {
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.form-control:focus {
  box-shadow: none;
  border-color: #888
}

.editbutton {
  width: 40%;
  margin-top: 5%;
  margin-left: 60%;
  background-color: white;
  border: 2px solid #888888;
  color: #888888;
}
.savebutton {
  width: 40%;
  margin-top: 5%;
  margin-right: 60%;
  background-color: white;
  border: 2px solid #888888;
  color: #888888;
}

.editbutton:hover {
  border: #888888;
  background-color: #888888;
  border: 2px solid #888888;
  color: white;
}

.savebutton:hover {
  background-color: #888888;
  border: 2px solid #888888;
  color: white;
}

.labels {
  font-size: 11px
}

.transparent-background {
  background-color: rgba(255, 255, 255, 0.2);
}

.navbar-container {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
}

.profile-box {
  position: absolute;
  top: 15%;
  right: 20%;
  left: 20%;
}

.clickable-text:hover {
  cursor: pointer;
  color: white !important;
}


</style>
