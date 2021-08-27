<template>
  <div>
    <h3>Project Management</h3>
    <p>
      <router-link to="/prj/addprj">Add</router-link>
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
          <b-button size="sm" @click="update(row.item, row.index, $event.target)" class="mr-1 mb-1" v-b-tooltip.hover :title="getUpdateTitle(row.item)">
            Update
          </b-button>
          <b-button size="sm" @click="updateEvData(row.item, row.index, $event.target)" class="mb-1" v-b-tooltip.hover title="Earned Value Data">
            <b-icon icon="bar-chart-steps"></b-icon>
          </b-button>
        </template>
        <template #thead-top="data">
          <b-tr>
            <b-th colspan="2"><span class="sr-only">Program</span></b-th>
            <b-th colspan="3" class="text-center">Associated</b-th>
            <b-th></b-th>
          </b-tr>
        </template>
        <template #cell(description)="data">
          <span class="textlines" v-b-popover.hover.top.html="'<pre>' + data.value + '</pre>'" title="Description">{{ data.value}}</span>
        </template>
        <!-- Popover for Strategic Goal -->
        <template #cell(strategicGoal)="data">
          <!--<h5><b-badge variant="info">{{data.value}}</b-badge></h5>-->
          <template v-if="data.value != ''">
            <h5><b-badge class="fullwidth" variant="info" v-b-popover.hover.html="getBadgeList(data.value)" title="Associated Strategic Goal">
              {{data.value}}</b-badge></h5>
          </template>
        </template>
        <!-- Popover for portfolio -->
        <template #cell(portfolio)="data">
          <!--<h5><b-badge variant="info">{{data.value}}</b-badge></h5>-->
          <template v-if="data.value != ''">
            <h5><b-badge class="fullwidth" variant="info" v-b-popover.hover.html="getBadgeList(data.value)" title="Associated Portfolio">
              {{data.value}}</b-badge></h5>
          </template>
        </template>
        <!-- Popover for program -->
        <template #cell(program)="data">
          <!--<h5><b-badge variant="info">{{data.value}}</b-badge></h5>-->
          <template v-if="data.value != ''">
            <h5><b-badge class="fullwidth" variant="info" v-b-popover.hover.html="getBadgeList(data.value)" title="Associated Program">
              {{data.value}}</b-badge></h5>
          </template>
        </template>
      </b-table>
    </div>
    <b-modal size="lg" :id="updateModal.id" :title="updateModal.title" @ok="handleOk">
      <formgroupprj :prj="updateModal.item" ref="modalupdateprj" mode="update" @ok="onSubmitted"></formgroupprj>
    </b-modal>
    <b-modal size="lg" :id="updateModal.idEvData" :title="updateModal.title" @hidden="handleEvDataHidden" ok-only>
      <prjevd :prj="updateModal.item" ref="modalevdata" @ok="onSubmitted"></prjevd>
    </b-modal>
  </div>
</template>

<script>
  var formgroupprj = httpVueLoader('components/formgroupprj.vue');
  var prjevd = httpVueLoader('components/prjevd.vue');

  module.exports = {
    data: function () {
      return {
        items: [],
        filter: null,
        fields: [
        { key: 'name', label: 'Project', sortable: true },
        { key: 'description', sortable: false },
        { key: 'portfolio', sortable: true },
        { key: 'program', sortable: true },
        { key: 'strategicGoal', label: 'Strategic Goal', sortable: true },
        { key: 'actions', label: 'Actions' }
        ],
        updateModal: {
          id: 'update-modal-prj',
          idEvData: 'update-modal-prj-evdata',
          title: '',
          item: '',
          orgItem: ''
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
        const response = await fetch('./getprj');
        if (response.ok) {
          const json = await response.json();
          this.items = json.data;
        }
      },
      getUpdateTitle(item) {
        return 'Update ' + item.name;
      },
      update(item, index, button) {
        this.updateModal.orgItem = item;
        this.updateModal.title = 'Update Project: ' + item.name;
        this.updateModal.item = JSON.parse(JSON.stringify(item)); // make a copy, assigning will be a reference
        this.$root.$emit('bv::show::modal', this.updateModal.id, button);
      },
      updateEvData(item, index, button) {
        this.updateModal.orgItem = item;
        this.updateModal.title = 'Earned Value Data: ' + item.name;
        this.updateModal.item = JSON.parse(JSON.stringify(item)); // make a copy, assigning will be a reference
        this.$root.$emit('bv::show::modal', this.updateModal.idEvData, button);
      },
      handleOk(bvModalEvt) {
        this.$refs.modalupdateprj.handleOk(bvModalEvt, this.updateModal.id);     
      },
      handleEvDataHidden() {
        if (this.updateModal.orgItem.earnedValues.length != this.updateModal.item.earnedValues.length) 
          this.onSubmitted(this.updateModal.item.name);
      },
      onSubmitted(prjname) {
        // TODO - should only updated the changed project
        this.fetchData().catch(error => {
          console.error(error)
        });
      }
    },
    components: {
      'formgroupprj': formgroupprj,
      'prjevd': prjevd
    }
  };
</script>