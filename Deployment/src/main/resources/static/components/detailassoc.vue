<template>
  <div>
    <b-card-group :columns="columns" :deck="deck">
      <template v-for="item in items">
        <b-card :header="item.name">
          <template v-if="item.owner != null">
            <span class="text-muted">Owner:</span>
            <p>{{item.owner}}</p>
          </template> 

          <template v-if="item.manager != null">
            <span class="text-muted">Manager:</span>
            <p>{{item.manager}}</p>
          </template>   

          <template v-if="item.currentState != null">
            <span class="text-muted">Status:</span>
            <p><strong>{{pf_state[item.currentState]}}</strong></p>
          </template>   

          <template v-if="item.priority != null && item.priority !=''">
            <span class="text-muted">Priority:</span>
            <p><b-badge pill class="pl-3 pr-3" :variant="priority_variant[priorities.indexOf(item.priority)]">{{item.priority}}</b-badge></p>
          </template>

          <span class="text-muted">Description:</span>
          <p>{{item.description}}</p>            
        </b-card>
      </template>    
    </b-card-group>  
    <!--<b-table
        :items="items"
        :fields="assoc.fields"
        bordered
        hover
    >    
        <template #cell(description)="data">
            <span class="textlines" v-b-popover.hover.top.html="'<pre>' + data.value + '</pre>'" title="Description">{{ data.value}}</span>
        </template>
    </b-table>-->
  </div>
</template>      

<script>
  module.exports = {
    props: {
      assoc: Object
    },
    data: function () {
      return {
        items: [],
        url: '',
        columns: false,
        deck: false,
      }
    },
    created() {
      if (this.assoc.type === 'pf') {
        this.url = './getpf?';
        this.assoc.title = 'Associated Portfolios';
      }
      else if (this.assoc.type === 'prj') {
        this.url = './getprj?';
        this.assoc.title = 'Associated Programs';
      }
      else if (this.assoc.type === 'prg') {
        this.url = './getprg?';
        this.assoc.title = 'Associated Programs';
      }
      else if (this.assoc.type === 'sg') {
        this.url = './getsg?';
        this.assoc.title = 'Associated Strategic Goal';
      }  
      else {
        alert ('Unknown type: ' + this.assoc.type);
        return;
      }

      this.fetchData().catch(error => {
        console.error(error)
      })
    },
    methods: {    
      async fetchData() {

        if (typeof(this.assoc.selected) === 'string') {
          const response = await fetch(this.url + new URLSearchParams({
            name: this.assoc.selected,
          }));
          if (response.ok) {
              const json = await response.json();
              this.items.push(json);
          }
        }
        else {
          for (const s in this.assoc.selected) {
              const response = await fetch(this.url + new URLSearchParams({
                name: this.assoc.selected[s],
              }));
              if (response.ok) {
                  const json = await response.json();
                  this.items.push(json);
              }
          }
        }

        if (this.items.length > 2)
          this.columns = true;
        else
          this.deck = true;
      },
    }
  };
</script>