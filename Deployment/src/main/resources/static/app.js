Vue.component('apexchart', VueApexCharts);
Vue.use(VueRouter);

var Home = httpVueLoader('components/home.vue');
var org = httpVueLoader('components/org.vue');
var addsg = httpVueLoader('components/addsg.vue');
var pfm = httpVueLoader('components/pfm.vue');
var addpf = httpVueLoader('components/addpf.vue');
var prg = httpVueLoader('components/prg.vue');
var addprg = httpVueLoader('components/addprg.vue');


const router = new VueRouter({
	/*mode: 'history',*/
	routes: [
		{
			path: '/',
			component: Home
		}, {
			path: '/org',
			name: 'org',
			component: org
		},{
			path: '/org/addsg',
			component: addsg
		},{
			path: '/pfm',
			name: 'pfm',
			component: pfm	
		}, {			
		    path: '/pfm/addpf',
			component: addpf
		}, {			
		    path: '/prg',
			name: 'prg',
			component: prg
		}, {			
		    path: '/prg/addprg',
			component: addprg
		}		
	]
});

const app = new Vue({
  router
}).$mount('#app');
