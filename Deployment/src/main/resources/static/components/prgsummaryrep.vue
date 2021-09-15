<template>
  <div>
    <b-table
      :items="items"
      :fields="fields"
      :sort-by.sync="sortBy"
      :sort-desc.sync="sortDesc"      
      sticky-header="true"
      responsive="sm"
      bordered
      hover
    >   
      <!--<template #cell(actions)="row">
        <b-button size="sm" @click="viewReport(row.item, row.index, $event.target)">
          View
        </b-button>
      </template> -->
      <template #cell(date)="data">
        <span> {{formatDate(data.value)}}</span>
      </template>
      <template #cell(trafficLight)="data">
        <b-button class="circle" :variant="status_variant[status_colour.indexOf(data.value)]"></b-button>
      </template>
    </b-table>
    <div v-if="lastItems.length">
      <b-card class="mb-3" sub-title="Summary">
        <b-row>
          <b-col md="2">
            <b-button class="circle" :variant="status_variant[status_colour.indexOf('RED')]"></b-button> <span>{{status.red}}</span>
          </b-col>
          <b-col md="2">
            <b-button class="circle" :variant="status_variant[status_colour.indexOf('AMBER')]"></b-button> <span>{{status.amber}}</span>
          </b-col>
          <b-col md="2">
            <b-button class="circle" :variant="status_variant[status_colour.indexOf('GREEN')]"></b-button> <span>{{status.green}}</span>
          </b-col>
        <b-row>
      </b-card>
    </div>
    <b-button @click="generate">Generate</b-button>
    <b-toast id="toast-no-reports" title="Generate Results" toaster="b-toaster-top-center" solid variant="danger">
      No Performance Reports found, please enter Earned Value Data for the associated Projects.
    </b-toast>
  </div>
</template>

<script>
  module.exports = {
    props: {
      program: String	    
    },
    data: function () {
      return {
        sortBy: 'date',
        sortDesc: false,
        items: [],
        lastItems: [],
        fields: [
          { key: 'date', label: 'Date', sortable: true },
          { key: 'project', label: 'Project', sortable: true },
          { key: 'trafficLight', label: 'Status', sortable: true }
        ],
        status: {
          red: null,
          amber: null,
          green: null
        }
      }
    },
    created() {
      this.fetchData().catch(error => {
        console.error(error)
      })
    },
    methods: {      
      async fetchData() {
        const response = await fetch('./getprgsr?' + new URLSearchParams({
          name: this.program,
        }));
        if (response.ok) {
          const json = await response.json();
          this.items = json.data;

          if (this.items.length === 0 && this.status.red !== null)
            this.$bvToast.show('toast-no-reports');
            //alert('No Performance Reports found, please enter Earned Value Data for the associated Projects.');

          this.lastItems = [];

          for (const i in this.items) {
            let found = this.lastItems.findIndex(x => x.project === this.items[i].project);
            if (found === -1)
              this.lastItems.push(this.items[i]);
            else {
              if (this.items[i].date > this.lastItems[found].date) {
                this.lastItems[found].date = this.items[i].date;
                this.lastItems[found].trafficLight = this.items[i].trafficLight;
              }
            }
          }

          this.status.red = 0;
          this.status.amber = 0;
          this.status.green = 0;
          for (const i in this.lastItems) {
            if (this.lastItems[i].trafficLight === 'RED')
              this.status.red += 1;
            else if (this.lastItems[i].trafficLight === 'AMBER')
              this.status.amber += 1;
            else if (this.lastItems[i].trafficLight === 'GREEN')
              this.status.green += 1;              
          }
        }
      },
      async generate() {
        const response = await fetch('./producesr?' + new URLSearchParams({
          name: this.program}), {
          method: 'POST',
          headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(this.prg)
        });
        if (response.ok) {
          setTimeout(this.fetchData, 100);   // need a delay for table to populate...
        }
      },
      formatDate(timestamp) {
        //return new Date(timestamp).toISOString().slice(0, 19).replace('T', ' ');
        let date = new Date(timestamp);
        var dateString =
          date.getFullYear() + '-' +
            ("0" + (date.getMonth()+1)).slice(-2) + "-" +
            ("0" + date.getDate()).slice(-2) + " " +
            //return HH:MM:SS with localtime without surprises
            date.toLocaleTimeString();

        return dateString;
      }
    }
  };
</script>