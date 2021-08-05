<template>
  <div>
    <h3>Strategic Goals</h3>
    <p>
      <router-link to="/org/addsg">Add</router-link>
    </p>
    <div>
      <b-row>
        <b-col lg="6" class="my-1">
          <b-form-group
            label="Filter"
            label-for="filter-input"
            label-cols-sm="3"
            label-align-sm="right"
            label-size="sm"
            class="mb-0"
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
      </b-table>
    </div>

    <b-modal :id="updateModal.id" :title="updateModal.title" @ok="handleOk" @show="resetModal">
      <form ref="form" @submit.stop.prevent="handleSubmit">
        <b-form-group label="Goal Identity:" label-for="input-1">
          <b-form-input id="input-1" v-model="updateModal.item.name" type="text" disabled></b-form-input>
        </b-form-group>

        <b-form-group label="Description:" label-for="input-2" invalid-feedback="Description is required"
          :state="updateModal.descState">
          <b-form-input id="input-2" v-model.trim="updateModal.item.description" :state="updateModal.descState" required></b-form-input>
        </b-form-group>

        <b-form-group label="Priority:" label-for="input-3">
          <b-form-select id="input-3" v-model="updateModal.item.priority" :options="updateModal.priorities"></b-form-select>
        </b-form-group>
      </form>
    </b-modal>
  </div>
</template>

<script>
  module.exports = {
    data: function () {
      return {
        items: [],
        filter: null,
        updateModal: {
          id: 'update-modal',
          title: '',
          priorities: ['HIGH', 'MEDIUM', 'LOW'],
          descState: null,
          item: '',
          orgItem: ''
        },
        fields: [
        { key: 'name', label: 'Goal Identity', sortable: true },
        { key: 'description', sortable: false },
        { key: 'priority', sortable: true },
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
      checkFormValidity() {
        const valid = this.$refs.form.checkValidity()
        this.updateModal.descState = valid
        return valid
      },
      resetModal() {
        this.updateModal.descState = null
      },
      handleOk(bvModalEvt) {
        // Prevent modal from closing
        bvModalEvt.preventDefault()
        // Trigger submit handler
        this.handleSubmit()
      },
      async handleSubmit(bvModalEvt) {
        if (!this.checkFormValidity()) {
          return
        }

        const response = await fetch('./updatesg', {
          method: 'POST',
          headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({ name: this.updateModal.orgItem.name, description: this.updateModal.item.description, priority: this.updateModal.item.priority })
        });

        this.updateModal.orgItem.description = this.updateModal.item.description;
        this.updateModal.orgItem.priority = this.updateModal.item.priority;

        // Hide the modal manually
        this.$nextTick(() => {
          this.$bvModal.hide(this.updateModal.id)
        })
      }
    }
  };
</script>