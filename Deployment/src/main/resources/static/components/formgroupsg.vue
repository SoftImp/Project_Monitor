<template>
  <div>
    <b-form-group label="Goal Identity:" label-for="input-1" invalid-feedback="Name is required" :state="state.name">
        <template v-if="mode === 'add'">
          <b-form-input id="input-1" ref="name" v-model.trim="sg.name" :state="state.name" required></b-form-input>
        </template>
        <template v-else>
          <b-form-input id="input-1" ref="name" :value="sg.name" readonly="true" :state="state.name" required></b-form-input>
        </template>
    </b-form-group>

    <b-form-group label="Description:" label-for="input-2" invalid-feedback="Description is required" :state="state.desc">
        <!--<b-form-input id="input-2" ref="description" v-model.trim="sg.description" :state="state.desc" required></b-form-input>-->
		  <b-form-textarea id="input-2" ref="description" v-model.trim="sg.description" :state="state.desc" required></b-form-textarea>
    </b-form-group>     
	
	<b-form-group id="input-group-3" label="Priority:" label-for="input-3" :state="state.priority">
        <b-form-select id="input-3" ref="priority" v-model="sg.priority" :options="priorities" :state="state.priority" required></b-form-select>
    </b-form-group>
  </div>
</template>

<script>
  module.exports = {
    props: {
      sg: Object,
      mode: {
        type: String,
        default: 'add'
      }
    },
    data: function () {
      return {
        state: {
            name: null,
            desc: null,
			      priority: null
        },
		    priorities: priorities
      }
    },
    methods: { 
      checkFormValidity() {
        this.state.name = this.$refs.name.checkValidity();
        this.state.desc = this.$refs.description.checkValidity();
		    //this.state.priority = this.$refs.priority.checkValidity();
		    this.state.priority = (this.sg.priority == '') ? false : true;

        for (const s in this.state) {
          if (this.state[s] == false)
            return false;
        }

        return true;
      },
      handleOk(bvModalEvt, modalId) {
        // Prevent modal from closing
        if (bvModalEvt)
          bvModalEvt.preventDefault();
        // Trigger submit handler
        this.handleSubmit(modalId);
      },
      async handleSubmit(modalId) {
        if (!this.checkFormValidity()) {
          return;
        }

        let url = './addsg';
        if (this.mode === 'update')
          url = '/updatesg';

        const response = await fetch(url, {
          method: 'POST',
          headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(this.sg)
        });

        this.$emit('ok', this.sg.name);  // Notify parent

        if (modalId) {
          // Hide the modal manually
          this.$nextTick(() => {
            this.$bvModal.hide(modalId)
          })
        }
      }
    }
  };
</script>