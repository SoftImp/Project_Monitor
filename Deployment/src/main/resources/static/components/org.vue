<template>
  <div>
    <h3>Strategic Goals</h3>
    <p>
      <router-link to="/org/addsg">Add</router-link>
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
            Update
          </b-button>
        </template>
        <template #thead-top="data">
          <b-tr>
            <b-th colspan="3"><span class="sr-only">Program</span></b-th>
            <b-th colspan="3" class="text-center">Associated</b-th>
            <b-th></b-th>
          </b-tr>
        </template>
        <!-- Popover for description -->
        <template #cell(description)="data">
          <span class="textlines" v-b-popover.hover.top.html="'<pre>' + data.value + '</pre>'" title="Description">{{ data.value}}</span>
        </template>
        <template #cell(priority)="data">
          <b-badge pill class="fullwidth" :variant="priority_variant[priorities.indexOf(data.value)]">{{data.value}}</b-badge>
        </template>
        <!-- Popover for portfolios -->
        <template #cell(portfolios)="data">
          <template  v-if="data.value.length > 0">
            <h5><b-badge class="fullwidth" variant="info" v-b-popover.hover.html="getBadgeList(data.value)" title="Associated Portfolios">
              <b-badge class="floatright" variant="light">{{data.value.length}}</b-badge>{{data.value[0]}}</b-badge></h5>
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
      <formgroupsg :sg="updateModal.item" ref="modalupdatesg" mode="update" @ok="onSubmitted"></formgroupsg>
    </b-modal>
  </div>
</template>

<script>
  var formgroupsg = httpVueLoader('components/formgroupsg.vue');

  module.exports = {
    data: function () {
      return {
        items: [],
        filter: null,
        updateModal: {
          id: 'update-modal',
          title: '',
          item: '',
          orgItem: ''
        },
        fields: [
        { key: 'name', label: 'Goal Identity', sortable: true },
        { key: 'description', sortable: false },
        { key: 'priority', sortable: true },
        { key: 'portfolios', sortable: true },
        { key: 'programs', sortable: true },
        { key: 'projects', sortable: true },
        { key: 'actions', label: 'Actions' }
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
        const response = await fetch('./getsg');
        if (response.ok) {
          const json = await response.json();
          this.items = json.data;
        }
      },
      update(item, index, button) {
        this.updateModal.orgItem = item;
        this.updateModal.title = 'Update Strategic Goal: ' + item.name;
        this.updateModal.item = JSON.parse(JSON.stringify(item)); // make a copy, assigning will be a reference
        this.$root.$emit('bv::show::modal', this.updateModal.id, button);
      },
      resetModal() {
        this.updateModal.descState = null
      },
      handleOk(bvModalEvt) {
        this.$refs.modalupdatesg.handleOk(bvModalEvt, this.updateModal.id);
      },
      onSubmitted(sgname) {
        // TODO - should only updated the changed strategic goal
        this.fetchData().catch(error => {
          console.error(error)
        });
      }
    },
    components: {
      'formgroupsg': formgroupsg
    }
  };
</script>