<template>
  <div>
    <h3>Portfolio Management</h3>
    <p>
      <router-link to="/pfm/addpf">Add</router-link>
    </p>
    <div>
      <b-row>
        <b-col lg="10" class="my-1">
          <b-form-group
            label="Filter"
            label-for="filter-input"
            label-cols-sm="3"
            label-align-sm="right"
            label-size="sm"
            class="mb-1"
          >
            <b-input-group size="sm">
              <b-form-input
                id="filter-input"
                v-model="filter"
                type="search"
                placeholder="Type to search..."
              ></b-form-input>

              <b-input-group-append>
                <b-button :disabled="!filter" @click="filter = ''">Clear</b-button>
              </b-input-group-append>
            </b-input-group>
          </b-form-group>
        </b-col>
      </b-row>

      <b-table bordered hover :items="items" :fields="fields" :filter="filter" head-variant="light">
        <template #cell(actions)="row">
          <b-button size="sm" @click="update(row.item, row.index, $event.target)" class="mr-1">
            <!--Update-->
            {{pf_state[row.item.currentState]}}
          </b-button>
          <!--<b-button size="sm" @click="row.toggleDetails">
            {{ row.detailsShowing ? '<' : '>' }}
          </b-button> -->
          <b-form-checkbox size="sm" class="mt-1" v-model="row.detailsShowing" @change="row.toggleDetails">
            Details
          </b-form-checkbox>
        </template>
        <template #thead-top="data">
          <b-tr>
            <b-th colspan="3"><span class="sr-only">Portfolio</span></b-th>
            <b-th colspan="3" class="text-center">Associated</b-th>
            <b-th></b-th>
          </b-tr>
        </template>
        <template #row-details="row">
          <detailpf :pf="row.item"></detailpf>   
        </template>
        <!-- Popover for description -->
        <template #cell(description)="data">
            <span class="textlines" v-b-popover.hover.top.html="'<pre>' + data.value + '</pre>'" title="Description">{{data.value}}</span>
        </template>
        <!-- Popover for Strategic Goal -->
        <template #cell(strategicGoal)="data">
          <!--<h5><b-badge variant="info">{{data.value}}</b-badge></h5>-->
          <template v-if="data.value != ''">
            <h5><b-badge class="fullwidth" variant="info" v-b-popover.hover.html="getBadgeList(data.value)" title="Associated Strategic Goal">
              {{data.value}}</b-badge></h5>
            </template>
        </template>
        <!-- Popover for programs -->
        <template #cell(programs)="data">
          <template  v-if="data.value.length > 0">
            <h5><b-badge class="fullwidth" variant="info" v-b-popover.hover.html="getBadgeList(data.value)" title="Associated Programs">
              <b-badge class="floatright" variant="light">{{data.value.length}}</b-badge>{{data.value[0]}}</b-badge></h5>
          </template>
        </template>
        <!-- Popover for projects -->
        <template #cell(projects)="data">
          <template  v-if="data.value.length > 0">
            <h5><b-badge class="fullwidth" variant="info" v-b-popover.hover.html="getBadgeList(data.value)" title="Associated Projects">
              <b-badge class="floatright" variant="light">{{data.value.length}}</b-badge>{{data.value[0]}}</b-badge></h5>
          </template>
        </template>
      </b-table>
    </div>

    <b-modal size="lg" :id="updateModal.id" :title="updateModal.title" @ok="handleOk">
      <formgrouppf :pf="updateModal.item" ref="modalupdatepf" :mode="updateModal.mode" @ok="onSubmitted"></formgrouppf>

      <template #modal-footer="{ cancel, ok }">
        <!-- Emulate built in modal footer ok and cancel button actions -->
        <b-button variant="secondary" @click="cancel()">
          Cancel
        </b-button>
        <b-button variant="primary" v-if="pf_state[updateModal.item.currentState] !== 'Closed'" @click="ok()">
          Save
        </b-button>
        <b-button variant="success" v-if="pf_state[updateModal.item.currentState] !== 'Closed'" @click="onComplete">
          <template v-if="pf_state[updateModal.item.currentState] !== 'Executing'">
            Complete
          </template>
          <template v-else>
            Close
          </template>  
        </b-button>
        <template v-if="pf_state[updateModal.item.currentState] === 'Executing'">
          <b-button variant="danger" @click="onReplan">
            Replan
          </b-button>
        </template>
        <template v-if="pf_state[updateModal.item.currentState] === 'Closed'">
          <b-button variant="danger" @click="onReactivate">
            Reactivate
          </b-button>
        </template>
      </template>
    </b-modal>
  </div>
</template>

<script>
  var formgrouppf = httpVueLoader('components/formgrouppf.vue');
  var detailpf = httpVueLoader('components/detailpf.vue');

  module.exports = {
    data: function () {
      return {
        items: [],
        filter: null,
        updateModal: {
          id: 'update-modal',
          idPlanning: 'update-modal-planning',
          title: '',
          item: '',
          orgItem: '',
          mode: ''
        },
        fields: [
        { key: 'name', label: 'Portfolio', sortable: true },
        { key: 'description', sortable: false },
        { key: 'manager', sortable: true },
        { key: 'strategicGoal', label: 'Strategic Goal', sortable: true },
        { key: 'programs', sortable: true },
        { key: 'projects', sortable: true },
        { key: 'actions', label: 'Status', tdClass: 'text-left' }
        ]
      }
    },
    created() {
    this.fetchData().catch(error => {
      console.error(error)
    })
    },
    methods: {
      async fetchData() {
        const response = await fetch('./getpf');
        if (response.ok) {
          const json = await response.json();
          this.items = json.data;
        }
      },
      update(item, index, button) {
        this.updateModal.orgItem = item;
        this.updateModal.item = JSON.parse(JSON.stringify(item)); // make a copy, assigning will be a reference

        if (pf_state[item.currentState] === 'Initialise')
          this.updateModal.title = 'Initialise Portfolio: ' + item.name;
        else if (pf_state[item.currentState] === 'Planning') 
          this.updateModal.title = 'Planning/Optimising Portfolio: ' + item.name;
        else if (pf_state[item.currentState] === 'Executing')
          this.updateModal.title = 'Executing Portfolio: ' + item.name;
        else if (pf_state[item.currentState] === 'Closed')
          this.updateModal.title = 'Closed Portfolio: ' + item.name;
        else {
          alert('Unknown Portfolio state!');
          return;
        }

        this.updateModal.mode = pf_state[item.currentState];
        this.$root.$emit('bv::show::modal', this.updateModal.id, button);
      },     
      handleOk(bvModalEvt) {
        this.$refs.modalupdatepf.handleOk(bvModalEvt, this.updateModal.id, 'save');
      },
      onSubmitted(pfname) {
        //for (const i in this.updateModal.orgItem)
          //this.updateModal.orgItem[i] = this.updateModal.item[i];
        // TODO - should only updated the changed portfolio
        this.fetchData().catch(error => {
          console.error(error)
        });
      },
      onComplete() {
        let action = 'save';

        if (pf_state[this.updateModal.orgItem.currentState] === 'Initialise')
          action = 'initcomplete';
        else if (pf_state[this.updateModal.orgItem.currentState] === 'Planning')  
          action = 'plancomplete';
        else if (pf_state[this.updateModal.orgItem.currentState] === 'Executing') 
          action = 'execomplete';

        this.$refs.modalupdatepf.handleOk(null, this.updateModal.id, action);
      },
      onReplan() {
        this.$refs.modalupdatepf.handleOk(null, this.updateModal.id, 'replan');
      },
      onReactivate() {
        this.$refs.modalupdatepf.handleOk(null, this.updateModal.id, 'reactivate');
      }
    },
    components: {
      'formgrouppf': formgrouppf,
      'detailpf': detailpf
    }
  };
</script>