import Vue from 'vue'
import Router from 'vue-router'
import Home from '../components/shared/Home'
import SignUp from "../components/shared/SignUp"
import Login from "../components/shared/Login"
import CustomerHome from "../components/customer/CustomerHome"
import CustomerAccount from "../components/customer/CustomerAccount"
import OwnerAddShift from "../components/owner/OwnerAddShift";
import ownerManageRooms from "../components/owner/ownerManageRooms";
import CustomerMakeRequest from "../components/customer/CustomerMakeRequest";
import ReservationCustomer from "../components/customer/ReservationCustomer";
import EmployeeHome from "../components/employee/EmployeeHome"
import EmployeeAccount from "../components/employee/EmployeeAccount"
import EmployeeRepair from "../components/employee/EmployeeRepair.vue";
import OwnerHome from "../components/owner/OwnerHome"
import OwnerAccount from "../components/owner/OwnerAccount"
import OwnerManageEmployees from "../components/owner/OwnerManageEmployees"
import OwnerViewSchedule from "../components/owner/OwnerViewSchedule"
import OwnerRepair  from "../components/owner/OwnerRepair.vue";
import EmployeeViewSchedule from "../components/employee/EmployeeViewSchedule";
import EmployeeReservation from "../components/employee/EmployeeReservation";
import OwnerRepair from "../components/owner/OwnerRepair.vue";

Vue.use(Router)

export default new Router({
  routes: [

    {
      path: '/Home/',
      component: Home
    },
    {
      path: '/SignUp/',
      component: SignUp
    },
    {
      path: '/Login/',
      component: Login
    },
    {
      path: '/CustomerHome/:param1',
      component: CustomerHome,
    },
    {
      path: '/CustomerAccount/:param1',
      component: CustomerAccount,
    },
    {
      path: '/EmployeeHome/:param1/:param2',
      component: EmployeeHome,
    },
    {
      path: '/EmployeeAccount/:param1/:param2',
      component: EmployeeAccount,
    },
    {
      path: '/EmployeeRepair/:param1/:param2',
      component: EmployeeRepair,
    },

    {
      path: '/owner',
      name: 'OwnerAddShift',
      component: OwnerAddShift
    },
    {
      path: '/owner/manage_rooms',
      name: 'ownerManageRooms',
      component: ownerManageRooms
    },
    {
      path: '/customer/:param1/reservation',
      name: 'ReservationCustomer',
      component: ReservationCustomer
    },
    {
      path: '/customer/:param1/reservation/:param2/make_request',
      name: 'CustomerMakeRequest',
      component: CustomerMakeRequest
    },
    {
      path: '/owner-view-schedule',
      name: 'OwnerViewSchedule',
      component: OwnerViewSchedule,
    },
    {
      path: '/employee-view-schedule',
      name: 'EmployeeViewSchedule',
      component: EmployeeViewSchedule,
    },
    {
     path: '/OwnerHome/:param1',
      component: OwnerHome,
    },

    {
      path: '/OwnerAccount/:param1',
      component: OwnerAccount,
    },

    {
      path: '/OwnerManageEmployees/:param1',
      component: OwnerManageEmployees,
    },

    {
      path: '/OwnerRepair/:param1',
      component: OwnerRepair,
    },
    {
      path: '/employee-manage-reservations',
      name: 'EmployeeReservation',
      component: EmployeeReservation,
    },
    {
      path: '/OwnerRepair/:param1',
      component: OwnerRepair,
    },
  ]
})
