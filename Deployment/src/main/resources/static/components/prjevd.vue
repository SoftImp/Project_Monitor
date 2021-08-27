<template>
  <div>
    <b-table
      :items="prj.earnedValues"
      :fields="fields"
      responsive="sm"
      selectable
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
  </div>
</template>

<script>
  var formgroupev = httpVueLoader('components/formgroupev.vue');

  module.exports = {
    props: {
      prj: Object	    
    },
    data: function () {
      return {
        items: [],
        filter: null,
        rowselected: [],
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
          item: '',
        },
        fields: [
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
      },
      handleEvOk(bvModalEvt) {
        this.$refs.modaladdev.handleOk(bvModalEvt, 'modal-addev');     
      },
      viewReport(item, index, button) {
        this.reportModal.item = item;
        this.reportModal.title = 'Performance Report: ' + this.prj.name;
        this.$root.$emit('bv::show::modal', this.reportModal.id, button);
      }
    },
    components: {
      'formgroupev': formgroupev
    }
  };
</script>