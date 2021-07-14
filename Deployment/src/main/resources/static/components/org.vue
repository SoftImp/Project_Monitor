<template>
  <div>
    <h3>Strategic Goals</h3>
    <p>
      <router-link to="/org/addsg">Add</router-link>
    </p>
    <table class="table table-striped">
      <thead>
        <tr>
          <th>Goal Identity</th>
          <th>Description</th>
          <th>Priority</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>Goal1</td>
          <td>Goal1 description</td>
          <td>High</td>
          <td><a href="#">Update</a></td>
        </tr>
        <tr>
          <td>Goal2</td>
          <td>Goal2 description</td>
          <td>Medium</td>
          <td><a href="#">Update</a></td>
        </tr>
        <tr>
          <td>Goal3</td>
          <td>Goal3 description</td>
          <td>Low</td>
          <td><a href="#">Update</a></td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
  module.exports = {
    data: function () {
      return {
        who: 'world'
      }
    }
  };
</script>