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
        </div>
      </template>
    </vuetable>

    <b-modal id="modal-updatesg" ref="modal" title="Update Strategic Goal" @ok="handleOk" @show="resetModal">
      <form ref="form" @submit.stop.prevent="handleSubmit">
        <b-form-group label="Goal Identity:" label-for="input-1">
          <b-form-input id="input-1" v-model="modal.goalId" type="text" disabled></b-form-input>
        </b-form-group>

        <b-form-group label="Description:" label-for="input-2" invalid-feedback="Description is required"
          :state="descState">
          <b-form-input id="input-2" v-model="modal.description" :state="descState" required></b-form-input>
        </b-form-group>

        <b-form-group label="Priority:" label-for="input-3">
          <b-form-select id="input-3" v-model="modal.priority" :options="priorities"></b-form-select>
        </b-form-group>
      </form>
    </b-modal>
  </div>
</template>

<script>
  module.exports = {
    data: function () {
      return {
        updatesg: '',
        modal: '',
        priorities: ['HIGH', 'MEDIUM', 'LOW'],
        descState: null,
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
      checkFormValidity() {
        const valid = this.$refs.form.checkValidity()
        this.descState = valid
        return valid
      },
      resetModal() {
        this.descState = null
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
          body: JSON.stringify({ goalId: this.updatesg.goalId, description: this.modal.description, priority: this.modal.priority })
        });

        this.updatesg.description = this.modal.description;
        this.updatesg.priority = this.modal.priority;

        // Hide the modal manually
        this.$nextTick(() => {
          this.$bvModal.hide('modal-updatesg')
        })
      }
    }
  };
</script>