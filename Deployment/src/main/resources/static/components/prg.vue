<template>
  <div>
    <h3>Program Management</h3>
    <p>
      <router-link to="/prg/addprg">Add</router-link>
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
          <!--<b-button size="sm" @click="update(row.item, row.index, $event.target)" class="mr-1 mb-1" v-b-tooltip.hover :title="getUpdateTitle(row.item)">
            Update
          </b-button>
          <b-button size="sm" @click="summaryReport(row.item, row.index, $event.target)" class="mb-1" v-b-tooltip.hover title="Summary Report">
            <b-icon icon="journal-bookmark"></b-icon>
          </b-button>-->
          <b-dropdown size="sm" split right text="Update" @click="update(row.item, row.index, $event.target)" split-variant="outline-secondary">
            <b-dropdown-item-button @click="summaryReport(row.item, row.index, $event.target)">Summary Reports</b-dropdown-item-button>
          </b-dropdown>
        </template>
        <template #thead-top="data">
          <b-tr>
            <b-th colspan="3"><span class="sr-only">Program</span></b-th>
            <b-th colspan="3" class="text-center">Associated</b-th>
            <b-th></b-th>
          </b-tr>
        </template>
        <template #cell(description)="data">
          <span class="textlines" v-b-popover.hover.top.html="'<pre>' + data.value + '</pre>'" title="Description">{{ data.value}}</span>
        </template>
        <!-- Popover for Strategic Goal -->
        <template #cell(strategicGoal)="data">
          <template v-if="data.value != ''">
            <h5><b-badge class="fullwidth" role="button" variant="info" @click="showAssoc(data.value, 'sg')" v-b-popover.hover.html="getBadgeList(data.value)" title="Associated Strategic Goal">
              {{data.value}}</b-badge></h5>
          </template>
        </template>
        <!-- Popover for portfolio -->
        <template #cell(portfolio)="data">
          <template v-if="data.value != ''">
            <h5><b-badge class="fullwidth" role="button" variant="info" @click="showAssoc(data.value, 'pf')" v-b-popover.hover.html="getBadgeList(data.value)" title="Associated Portfolio">
              {{data.value}}</b-badge></h5>
          </template>
        </template>
        <!-- Popover for projects -->
        <template #cell(projects)="data">
          <template  v-if="data.value.length > 0">
            <h5><b-badge class="fullwidth" role="button" variant="info" @click="showAssoc(data.value, 'prj')" v-b-popover.hover.html="getBadgeList(data.value)" title="Associated Projects">
              <b-badge class="floatright" variant="light">{{data.value.length}}</b-badge>{{data.value[0]}}</b-badge></h5>
          </template>
        </template>
      </b-table>
    </div>

    <b-modal size="lg" :id="updateModal.id" :title="updateModal.title" @ok="handleOk">
      <formgroupprg :prg="updateModal.item" ref="modalupdateprg" mode="update" @ok="onSubmitted"></formgroupprg>
    </b-modal>
    <b-modal size="lg" :id="assoc_info.id"  :title="assoc_info.title" ok-only>
      <detailassoc :assoc="assoc_info"></detailassoc>
    </b-modal>
    <b-modal size="lg" :id="summaryRepModal.id"  :title="summaryRepModal.title" ok-only>
      <prgsummaryrep :program="summaryRepModal.program"></prgsummaryrep>
    </b-modal>
  </div>
</template>

<script>
  var formgroupprg = httpVueLoader('components/formgroupprg.vue');
  var detailassoc = httpVueLoader('components/detailassoc.vue');
  var prgsummaryrep = httpVueLoader('components/prgsummaryrep.vue');

  module.exports = {
    data: function () {
      return {
        items: [],
        filter: null,
        fields: [
        { key: 'name', label: 'Program', sortable: true },
        { key: 'description', sortable: false },
        { key: 'owner', sortable: true },
        { key: 'portfolio', sortable: true },
        { key: 'projects', sortable: true },
        { key: 'strategicGoal', label: 'Strategic Goal', sortable: true },
        { key: 'actions', label: 'Actions' }
        ],
        updateModal: {
          id: 'update-modal-prg',
          title: '',
          item: '',
          orgItem: ''
        },
        assoc_info: {
          title: 'Associated',
          id: 'modal-detailassoc',
          type: '',
          selected: [],
        },
        summaryRepModal: {
          id: 'summary-rep-modal',
          title: '',
          program: ''
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
        const response = await fetch('./getallprg');
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
        this.updateModal.title = 'Update Program: ' + item.name;
        this.updateModal.item = JSON.parse(JSON.stringify(item)); // make a copy, assigning will be a reference
        this.$root.$emit('bv::show::modal', this.updateModal.id, button);
      },
      summaryReport(item, index, button) {
        this.summaryRepModal.title = 'Summary Reports: ' + item.name;
        this.summaryRepModal.program = item.name;
        this.$root.$emit('bv::show::modal', this.summaryRepModal.id, button);
      },
      showAssoc(selected, type) {
        this.assoc_info.type = type;
        this.assoc_info.selected = selected;

        this.$root.$emit('bv::show::modal', this.assoc_info.id);
      },
      onSubmitted(prgname) {
        // TODO - should only updated the changed program
        this.fetchData().catch(error => {
          console.error(error)
        });
      },
      handleOk(bvModalEvt) {
        this.$refs.modalupdateprg.handleOk(bvModalEvt, this.updateModal.id); 
      }
    },
    components: {
      'formgroupprg': formgroupprg,
      'detailassoc': detailassoc,
      'prgsummaryrep': prgsummaryrep
    }
  };
</script>