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

      <b-table striped bordered hover :items="items" :fields="fields" :filter="filter">
        <template #cell(actions)="row">
          <b-button size="sm" @click="update(row.item, row.index, $event.target)" class="mr-1">
            Update
          </b-button>
        </template>
        <!--<template #thead-top="data">
          <b-tr>
            <b-th colspan="3"><span class="sr-only">Program</span></b-th>
            <b-th colspan="3" class="text-center">Associated</b-th>
            <b-th></b-th>
          </b-tr>
        </template> -->
      </b-table>
    </div>
  </div>
</template>

<script>

  module.exports = {
    data: function () {
      return {
        items: [],
        filter: null,
        fields: [
        { key: 'name', label: 'Program', sortable: true },
        { key: 'description', sortable: false },
        { key: 'owner', sortable: true },
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
        const response = await fetch('./getprg');
        if (response.ok) {
          const json = await response.json();
          this.items = json.data;
        }
      },
      update(item, index, button) {
        /*this.updateModal.orgItem = item;
        this.updateModal.title = 'Update Portfolio: ' + item.name;
        this.updateModal.item = JSON.parse(JSON.stringify(item)); // make a copy, assigning will be a reference
        this.$root.$emit('bv::show::modal', this.updateModal.id, button);*/
      }
     },
    components: {
      
    }
  };
</script>