<template>
  <div>
    <b-form-group :label="assoc.label">
      <b-input-group>
        <b-form-tags v-model="assoc.selected" size="lg" add-on-change placeholder="" onKeyPress="return false;"></b-form-tags>
          <b-input-group-append v-if="assoc.createid != ''">
            <b-button @click="handleCreateClick($event.target)" variant="primary">Create</b-button>
          </b-input-group-append>
        <b-input-group-append>
          <b-button @click="handleClick($event.target)" variant="secondary">Associate</b-button>
        </b-input-group-append>
      </b-input-group>
    </b-form-group>

    <!-- Modal -->
    <b-modal :id="assoc.id" ref="select-modal" :title="assoc.title" @ok="handleOk" @show="fetchData">
      <div>
        <b-table
          :items="items"
          :fields="assoc.fields"
          :select-mode="assoc.mode"
          responsive="sm"
          ref="selectableTable"
          selectable
          striped 
          bordered
          @row-selected="onRowSelected"
        >    
          <!--Example scoped slot for select state illustrative purposes -->
          <template #cell(selected)="{ rowSelected }">
            <template v-if="rowSelected">
              <span aria-hidden="true">&check;</span>
              <span class="sr-only">Selected</span>
            </template>
            <template v-else>
              <span aria-hidden="true">&nbsp;</span>
              <span class="sr-only">Not selected</span>
            </template>
          </template>
        </b-table>
      </div>
    </b-modal> 
    <b-modal v-if="assoc.createid == 'modal-createprg'" :id="assoc.createid"  title="Create Program" @ok="handleCreateOk">
      <formgroupprg :prg="prg" ref="modalcreate" @ok="createOk"></formgroupprg>
    </b-modal>   

    <b-modal v-if="assoc.createid == 'modal-createprj'" :id="assoc.createid"  title="Create Project" @ok="handleCreateOk">
      <formgroupprj :prj="prj" ref="modalcreate" @ok="createOk"></formgroupprj>
    </b-modal> 
  </div>
</template>

<script>
  var formgroupprg = httpVueLoader('components/formgroupprg.vue');
  var formgroupprj = httpVueLoader('components/formgroupprj.vue');

  module.exports = {
    props: {
      assoc: Object	    
    },
    data: function () {
      return {
        items: [],
        rowselected: [],
        prg: {
          name : '',
          description: '',
          owner: ''
        },
        prj: {
          name : '',
          description: ''
        }
      }
    },
    /*created() {
      this.fetchData().catch(error => {
        console.error(error)
      })
    },*/
    methods: {    
      async fetchData() {
        //this.items = [];
        const response = await fetch(this.assoc.url);
        if (response.ok) {
          const json = await response.json();
          this.items = json.data;
        }

        setTimeout(this.setSelected, 100);   // need a delay for table to populate...
      },
      async setSelected() {
        this.assoc.selected.forEach(name => this.selectRow(name));
      },
      onRowSelected(items) {
        this.rowselected = items
      },
      selectRow(name) {
        const row = this.items.findIndex(x => x.name === name);
        this.$refs.selectableTable.selectRow(row);
      },
      handleKey(){
        alert('key');
        this.$root.$emit('bv::show::modal', this.assoc.id);
      },
      handleClick(button) {
        this.$root.$emit('bv::show::modal', this.assoc.id, button);
      },
      handleCreateClick(button) {
        this.$root.$emit('bv::show::modal', this.assoc.createid, button);
      },
      handleOk(bvModalEvt) {
        // Prevent modal from closing
        bvModalEvt.preventDefault()
        // Trigger submit handler
        this.handleSubmit()
      },
      async handleSubmit(bvModalEvt) { 
	  //alert(JSON.stringify(this.$refs.vuetable.selectedTo));

        this.assoc.selected = [];
        this.rowselected.forEach(item => this.assoc.selected.push(item.name));

        this.$emit('ok', this.assoc.selected);  // Notify parent
		
        // Hide the modal manually
        this.$nextTick(() => {
          this.$bvModal.hide(this.assoc.id)
        })
      },
      handleCreateOk(bvModalEvt) {
        this.$refs.modalcreate.handleOk(bvModalEvt, this.assoc.createid);
      },
      createOk(name) {
        this.assoc.selected.push(name);
      }
    },
    components: {
      'formgroupprg': formgroupprg,
      'formgroupprj': formgroupprj
    }
  };
</script>