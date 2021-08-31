<template>
  <div>
    <b-card-group deck>
      <b-card title="Earned Value Data">
        <b-row>
          <b-col md="9" class="mb-2">
            <b-card-text>Budget At Completion:</b-card-text>
          </b-col>
          <b-col md="3">
            <b-card-text>{{parseFloat(pr.bac).toFixed(2)}}</b-card-text>
          </b-col>
        </b-row>
        <b-row>
          <b-col md="9" class="mb-2">
            <b-card-text>Earned Value:</b-card-text>
          </b-col>
          <b-col md="3">
            <b-card-text>{{parseFloat(pr.ev).toFixed(2)}}</b-card-text>
          </b-col>
        </b-row>
        <b-row>
          <b-col md="9" class="mb-2">
            <b-card-text>Planned Value:</b-card-text>
          </b-col>
          <b-col md="3">
            <b-card-text>{{parseFloat(pr.pv).toFixed(2)}}</b-card-text>
          </b-col>
        </b-row>
        <b-row>
          <b-col md="9" class="mb-2">
            <b-card-text>Actual Cost:</b-card-text>
          </b-col>
          <b-col md="3">
            <b-card-text>{{parseFloat(pr.ac).toFixed(2)}}</b-card-text>
          </b-col>
        </b-row>
      </b-card>
      <b-card title="Earned Value Variances">
        <b-row>
          <b-col md="9" class="mb-2">
            <b-card-text>Cost Variance:</b-card-text>
          </b-col>
          <b-col md="3">
            <b-card-text>{{parseFloat(pr.cv).toFixed(2)}}</b-card-text>
          </b-col>
        </b-row>
        <b-row>
          <b-col md="9" class="mb-2">
            <b-card-text>Schedule Variance:</b-card-text>
          </b-col>
          <b-col md="3">
            <b-card-text>{{parseFloat(pr.sv).toFixed(2)}}</b-card-text>
          </b-col>
        </b-row>
      </b-card>  
    </b-card-group>  
    <b-card-group deck class="mt-3">
      <b-card title="Performance Indices">
        <b-row>
          <b-col md="9" class="mb-2">
            <b-card-text>Cost Performance Index:</b-card-text>
          </b-col>
          <b-col md="3">
            <b-card-text>{{parseFloat(pr.cpi).toFixed(2)}}</b-card-text>
          </b-col>
        </b-row>
        <b-row>
          <b-col md="9" class="mb-2">
            <b-card-text>Schedule Performance Index:</b-card-text>
          </b-col>
          <b-col md="3">
            <b-card-text>{{parseFloat(pr.spi).toFixed(2)}}</b-card-text>
          </b-col>
        </b-row>
        <b-row>
          <b-col md="9" class="mb-2">
            <b-card-text>Status:</b-card-text>
          </b-col>
          <b-col md="3">
            <!--<b-card-text>{{pr.trafficLight}}</b-card-text>-->
            <b-button class="circle" :variant="status_variant[status_colour.indexOf(pr.trafficLight)]"></b-button>
          </b-col>
        </b-row>
      </b-card>  
      <b-card title="Completion Estimates">
        <b-row>
          <b-col md="9" class="mb-2">
            <b-card-text>Estimate At Completion:</b-card-text>
          </b-col>
          <b-col md="3">
            <b-card-text>{{parseFloat(pr.eac).toFixed(2)}}</b-card-text>
          </b-col>
        </b-row>
        <b-row>
          <b-col md="9" class="mb-2">
            <b-card-text>Variance At Completion:</b-card-text>
          </b-col>
          <b-col md="3">
            <b-card-text>{{parseFloat(pr.vac).toFixed(2)}}</b-card-text>
          </b-col>
        </b-row>
        <b-row>
          <b-col md="9" class="mb-2">
            <b-card-text>Estimate To Complete:</b-card-text>
          </b-col>
          <b-col md="3">
            <b-card-text>{{parseFloat(pr.etc).toFixed(2)}}</b-card-text>
          </b-col>
        </b-row>
      </b-card>
    </b-card-group>  
  </div>
</template>

<script>
  module.exports = {
    props: {
      project: String,
	    repid: Number
    },
    data: function () {
      return {
        pr: ''
      }
    },
    created() {
      this.fetchData().catch(error => {
        console.error(error)
      })
    },
  	methods: { 
      async fetchData() {
        const response = await fetch('./getperfrep?' + new URLSearchParams({
              project: this.project,
              repId: this.repid,
          }));
        if (response.ok) {
          const json = await response.json();
          this.pr = json;
        }
      },
		}
  };
</script>
	  