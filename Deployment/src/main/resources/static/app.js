Vue.component('apexchart', VueApexCharts);
Vue.use(BootstrapVue);
//Vue.use(BootstrapVueIcons);
Vue.use(VueRouter);

const pf_state = ["Initialise", "Closed", "Executing", "Planning"];
const priorities = [{ text: 'Select Priority ...', value: '' }, 'HIGH', 'MEDIUM', 'LOW'];
const priority_variant = ['', 'danger', 'primary', 'secondary'];

var Home = httpVueLoader('components/home.vue');
var org = httpVueLoader('components/org.vue');
var addsg = httpVueLoader('components/addsg.vue');
var pfm = httpVueLoader('components/pfm.vue');
var addpf = httpVueLoader('components/addpf.vue');
var prg = httpVueLoader('components/prg.vue');
var addprg = httpVueLoader('components/addprg.vue');
var prj = httpVueLoader('components/prj.vue');
var addprj = httpVueLoader('components/addprj.vue');

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
		}, {			
		    path: '/prj',
			name: 'prj',
			component: prj
		}, {			
		    path: '/prj/addprj',
			component: addprj
		}		
	]
});

Vue.mixin({
	methods: {
		getBadgeList: function (components) {
			let html = '<h5>';
			let badges = [];
	
			if (typeof(components) === 'string')
			  badges.push(components);
			else
			  badges = components;
			
			for (const s in badges)
			  html += '<span class="badge badge-info fullwidth">' + badges[s] + '</span>'
			html += '</h5>';
	
			return html;
		  }
	}
});

const app = new Vue({
  router
}).$mount('#app');
