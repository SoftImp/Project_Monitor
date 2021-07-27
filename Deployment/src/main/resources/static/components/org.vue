<template>
  <div>
    <h3>Strategic Goals</h3>
    <p>
      <router-link to="/org/addsg">Add</router-link>
    </p>
    <vuetable ref="vuetable" api-url="./getsg" :fields="fields">
      <template slot="actions" scope="props">
        <div class="custom-actions">
          <b-button v-b-modal="'modal-updatesg'" class="ui basic button"
            @click="onAction('edit-item', props.rowData, props.rowIndex)">
            <i class="edit icon"></i>
          </b-button>
          <!--<b-button v-b-modal.modal-1>Launch demo modal</b-button>-->
        </div>
      </template>
    </vuetable>

    <b-modal id="modal-updatesg" title="Update Strategic Goals" @ok="handleOk">
      <div v-if="errors.length">
        <b>Please correct the following error(s):</b>
      <ul>
        <li v-for="error in errors">{{ error }}</li>
      </ul>
      </div>
      <div class="col-md-12">
        <form @submit.stop.prevent="handleSubmit">
          <div class="mb-3">
            <label for="name" class="form-label">Goal Identity</label>
            <input type="text" v-model="modal.goalId" name="name" class="form-control" id="name" disabled="disabled">
          </div>
          <div class="mb-3">
            <label for="description" class="form-label">Description</label>
            <input type="text" v-model="modal.description" name="description" class="form-control" id="description">
          </div>
          <div class="mb-3">
            <label for="priority" class="form-label">Priority</label>
            <select class="form-select" id="priority" v-model="modal.priority" name="priority">
              <option value="Select" selected="true" disabled="disabled">Select Priority ...</option>
              <option value="HIGH">HIGH</option>
              <option value="MEDIUM">MEDIUM</option>
              <option value="LOW">LOW</option>
            </select>
          </div>
          <!--<button type="submit" class="btn btn-primary">Add</button> |
        <router-link to="/org">Back to List</router-link> -->
        </form>
      </div>
    </b-modal>
  </div>
</template>

<script>
  module.exports = {
    data: function () {
      return {
        errors: [],
        updatesg: '',
		modal: '',
        fields: [
          {
            name: 'goalId',
            title: 'Goal Identity'
          },
          'description',
          'priority',
          {
            name: '__slot:actions',
            title: 'Actions',
            titleClass: 'center aligned',
            dataClass: 'center aligned',
          }
        ]
      }
    },
    methods: {
      async onAction(action, data, index) {
		this.updatesg = data;
        this.modal = JSON.parse(JSON.stringify(data));	// make a copy, assigning will be a reference
        console.log('slot action: ' + action, data.goalId, index)
      },
      handleOk(bvModalEvt) {
        // Prevent modal from closing
        bvModalEvt.preventDefault()
        // Trigger submit handler
        this.handleSubmit()
      },
      async handleSubmit() {
        if (this.modal.description) {
          const response = await fetch('./updatesg', {
            method: 'POST',
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json'
            },
            body: JSON.stringify({ goalId: this.updatesg.goalId, description: this.modal.description, priority: this.modal.priority })
          });
		  
		  this.updatesg.description = this.modal.description;
		  this.updatesg.priority = this.modal.priority;

          // Hide the modal manually
          this.$nextTick(() => {
            this.$bvModal.hide('modal-updatesg')
          })
        }
        else {
          this.errors = [];

          if (!this.description) {
            this.errors.push('Description required.');
          }
        }
      }
    }
  };
</script>