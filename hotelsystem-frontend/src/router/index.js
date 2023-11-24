import Vue from 'vue'
import Router from 'vue-router'
import Home from '../components/shared/Home'
import SignUp from "../components/shared/SignUp"
import Login from "../components/shared/Login"
import CustomerHome from "../components/customer/CustomerHome"
import CustomerAccount from "../components/customer/CustomerAccount"
import OwnerViewSchedule from "../components/owner/OwnerViewSchedule"
import EmployeeViewSchedule from "../components/employee/EmployeeViewSchedule";

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    {
      path: '/',
      name: 'SignUp',
      component: SignUp
    },
    {
      path: '/',
      name: 'Login',
      component: Login
    },
    {
      path: '/',
      name: 'CustomerHome',
      component: CustomerHome,
      props: true
    },
    {
      path: '/',
      name: 'CustomerAccount',
      component: CustomerAccount,
      props: true
    },
    {
      path: '/',
      name: 'OwnerViewSchedule',
      component: OwnerViewSchedule,
      props: true
    },
    {
      path: '/',
      name: 'EmployeeViewSchedule',
      component: EmployeeViewSchedule,
      props: true
    },
  ]
})
