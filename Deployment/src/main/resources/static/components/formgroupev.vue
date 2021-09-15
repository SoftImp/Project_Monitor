<template>
  <div>
    <b-form-group label="Budget At Completion:" label-for="input-1" invalid-feedback="Budget At Completion is required" :state="state.bac">
      <b-form-input id="input-1" ref="bac" v-model.trim="ev.bac" :state="state.bac" type="number" step="any" min="0" required></b-form-input>
    </b-form-group>

    <b-form-group label="Earned Value:" label-for="input-2" invalid-feedback="Earned Value is required" :state="state.ev">
		  <b-form-input id="input-2" ref="ev" v-model.trim="ev.ev" :state="state.ev" type="number" step="any" min="0" required></b-form-input>
    </b-form-group>     
	
	  <b-form-group label="Planned Value:" label-for="input-3" invalid-feedback="Planned Value is required" :state="state.pv">
      <b-form-input id="input-3" ref="pv" v-model.trim="ev.pv" :state="state.pv" type="number" step="any" min="0" required></b-form-input>
    </b-form-group>

    <b-form-group label="Actual Cost:" label-for="input-4" invalid-feedback="Actual Cost is required" :state="state.ac">
      <b-form-input id="input-4" ref="ac" v-model.trim="ev.ac" :state="state.ac" type="number" step="any" min="0" required></b-form-input>
    </b-form-group>

    <b-toast id="toast-confirm" title="Confirmation" toaster="b-toaster-top-center" solid no-auto-hide variant="danger">
      <template #default>
        <div>
          <p>Please select OK to confirm that the data values entered are correct and to save the values to the system.</p>
          <p>Please select Cancel to continue editing.</p>
          <p><b>Please note that once saved, these values cannot be changed.</b></p>
          <footer class="modal-footer pb-0 pr-0">
            <b-button @click="$bvToast.hide('toast-confirm')">Cancel</b-button>
            <b-button variant="primary" @click="handleConfirm('toast-confirm')">OK</b-button>
          </footer>
        </div>
      </template>
    </b-toast>
  </div>
</template>

<script>
  module.exports = {
    props: {
      ev: Object,
      project: {
        type: String,
        default: ''
      }
    },
    data: function () {
      return {
        state: {
            bac: null,
            ev: null,
			      pv: null,
            ac: null,
        },
        modalId: null
      }
    },
    methods: { 
      checkFormValidity() {
        this.state.bac = this.$refs.bac.checkValidity();
        this.state.ev = this.$refs.ev.checkValidity();
        this.state.pv = this.$refs.pv.checkValidity();
        this.state.ac = this.$refs.ac.checkValidity();

        for (const s in this.state) {
          if (this.state[s] == false)
            return false;
        }

        return true;
      },
      handleConfirm(toastId) {
        this.$bvToast.hide(toastId);
        this.handleSubmit(this.modalId);
      },
      handleOk(bvModalEvt, modalId) {
        // Prevent modal from closing
        if (bvModalEvt)
          bvModalEvt.preventDefault();

        if (!this.checkFormValidity()) {
          return;
        }

        this.modalId = modalId;
        this.$bvToast.show('toast-confirm');
      },
      async handleSubmit(modalId) {
        let url = './addevdata?project=' + this.project;

        const response = await fetch(url, {
            method: 'POST',
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(this.ev)
          });

        this.$emit('ok', this.ev);  // Notify parent

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