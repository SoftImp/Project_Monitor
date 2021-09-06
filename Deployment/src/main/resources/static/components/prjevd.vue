<template>
  <div>
    <b-table
      :items="prj.earnedValues"
      :fields="fields"
      sticky-header="true"
      responsive="sm"
      bordered
      hover
    >   
      <template #cell(actions)="row">
        <b-button size="sm" @click="viewReport(row.item, row.index, $event.target)">
          View
        </b-button>
      </template> 
    </b-table>

    <b-button v-b-modal.modal-addev>Add</b-button>

    <b-modal id="modal-addev" title="Add Earned Value Data" @ok="handleEvOk">
      <formgroupev :ev="evData" :project="prj.name" ref="modaladdev" @ok="onEvSubmitted"></formgroupev>
    </b-modal>

    <b-modal size="lg" :id="reportModal.id" :title="reportModal.title" ok-only>
      <perfrep :project="reportModal.project" :repid="reportModal.repId" ></perfrep>
    </b-modal>
  </div>
</template>

<script>
  var formgroupev = httpVueLoader('components/formgroupev.vue');
  var perfrep = httpVueLoader('components/perfrep.vue');

  module.exports = {
    props: {
      prj: Object	    
    },
    data: function () {
      return {
        items: [],
        filter: null,
        evData: {
          repId: 0,
          bac: null,
          ev: null,
          pv: null,
          ac: null
        },
        reportModal: {
          id: 'report-modal',
          title: '',
          project: '',
          repId: 0
        },
        fields: [
          { key: 'repId', label: '#', sortable: true },
          { key: 'bac', label: 'Budget At Completion', sortable: true },
          { key: 'ev', label: 'Earned Value', sortable: true },
          { key: 'pv', label: 'Planned Value', sortable: true },
          { key: 'ac', label: 'Actual Cost', sortable: true },
          { key: 'actions', label: 'Rerformance Report' }
        ]
      }
    },
    methods: {      
      onEvSubmitted(ev) {
        this.evData.repId = this.prj.earnedValues.length + 1;
        this.prj.earnedValues.push(JSON.parse(JSON.stringify(ev)));

        for (const i in this.evData)
          this.evData[i] = null;
          
        this.evData.repId = 0;
        this.$nextTick(() => {
            this.viewReport(this.prj.earnedValues[this.prj.earnedValues.length-1], 0, null)
        })
      },
      handleEvOk(bvModalEvt) {
        this.$refs.modaladdev.handleOk(bvModalEvt, 'modal-addev');     
      },
      viewReport(item, index, button) {
        this.reportModal.repId = item.repId;
        this.reportModal.project = this.prj.name;
        this.reportModal.title = 'Performance Report: ' + this.prj.name + ' - ' + item.repId;
        this.$root.$emit('bv::show::modal', this.reportModal.id, button);
      }
    },
    components: {
      'formgroupev': formgroupev,
      'perfrep': perfrep
    }
  };
</script>