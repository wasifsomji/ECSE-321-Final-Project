import Vue from 'vue'
import Router from 'vue-router'
import Home from '../components/shared/Home'
import SignUp from "../components/shared/SignUp"
import Login from "../components/shared/Login"
import CustomerHome from "../components/customer/CustomerHome"
import CustomerAccount from "../components/customer/CustomerAccount"
import ReservationGuest from "../components/shared/ReservationGuest"
import CreateAccountSuggestion from "../components/shared/CreateAccountSuggestion"
import SuggestRooms from "../components/shared/SuggestRooms"
import CreateCustomerPage from "../components/shared/createCustomerPage"
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
import EmployeeViewSchedule from "../components/employee/EmployeeViewSchedule";
import EmployeeReservation from "../components/employee/EmployeeReservation";
import OwnerRepair from "../components/owner/OwnerRepair.vue";
import BookedConfirmation from "../components/shared/BookedConfirmation";
import CustomerReservation from "../components/customer/CustomerReservation.vue";
import SuggestRoomsCustomer from "../components/customer/SuggestRoomsCustomer.vue";
import ConfirmReservation from "../components/customer/ConfirmReservation.vue";
import BookedConfirmationCustomer from "../components/customer/BookedConfirmationCustomer.vue";
import OwnerReservation from "../components/owner/OwnerReservation";

Vue.use(Router)

export default new Router({
  routes: [

    {
      path: '/',
      name: 'Home',
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
      path: '/ReservationGuest',
      name: 'ReservationGuest',
      component: ReservationGuest
    },
    {
      path: '/CreateAccountSuggestion',
      name: 'CreateAccountSuggestion',
      component: CreateAccountSuggestion
    },
    {
      path: '/BookedConfirmationCustomer/:param1',
      name: 'BookedConfirmationCustomer',
      component: BookedConfirmationCustomer
    },
    {
      path: '/SuggestRooms/:param1/:param2/:param3/:param4/:param5',
      name: 'SuggestRooms',
      component: SuggestRooms
    },
    {
      path: '/CreateCustomerPage/:param1/:param2/:param3/:param4',
      name: 'CreateCustomerPage',
      component: CreateCustomerPage
    },
    {
      path: '/CustomerReservation/:param1',
      name: 'CustomerReservation',
      component: CustomerReservation
    },
    {
      path: '/SuggestRoomsCustomer/:param1/:param2/:param3/:param4/:param5/:param6',
      name: 'SuggestRoomsCustomer',
      component: SuggestRoomsCustomer
    },
    {
      path: '/ConfirmReservation/:param1/:param2/:param3/:param4/:param5',
      name: 'ConfirmReservation',
      component: ConfirmReservation
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
      path: '/owner/manage_rooms/:param1',
      name: 'ownerManageRooms',
      component: ownerManageRooms
    },
    {
      path: '/customer/reservation/:param1',
      name: 'ReservationCustomer',
      component: ReservationCustomer
    },
    {
      path: '/customer/reservation/make_request/:param1/:param2',
      name: 'CustomerMakeRequest',
      component: CustomerMakeRequest
    },
    {
      path: '/owner-view-schedule/:param1',
      component: OwnerViewSchedule,
    },
    {
      path: '/EmployeeSchedule/:param1/:param2',
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
      path: '/EmployeeReservation/:param1/:param2',
      component: EmployeeReservation,
    },
    {
      path: '/OwnerRepair/:param1',
      component: OwnerRepair,
    },

    {
      path: '/OwnerReservation/:param1/:param2',
      component: OwnerReservation,
    },

    {
      path: '/BookedConfirmation/',
      component: BookedConfirmation,
    },


  ]
})
