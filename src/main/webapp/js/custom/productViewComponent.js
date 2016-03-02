var ProductList = React.createClass({
  getInitialState: function() {
    return {data: []};
  },
  componentDidMount: function() {
    $.ajax({
      url: this.props.url,
      cache: false,
      success: function(data) {
        this.setState({data: data});
      }.bind(this),
      error: function(xhr, status, err) {
        console.error(this.props.url, status, err.toString());
      }.bind(this)
    });
  },
  render: function() {
      var productNodes = this.state.data.map(function(prod) {
        return (
          <Product name={prod.name} price={prod.price} image={prod.imageLink}/>
        );
      });
      return (
        <div id="products-list" className="container">
          {productNodes}
        </div>
      );
    }
});


var Product = React.createClass({
  render: function() {
    return (
    <div className="row">
        <div className="box">
          <div className="col-lg-12">
            <hr />
            <h2 className="intro-text text-center">
                {this.props.name}
            </h2>
            <hr />
          </div>
          <div className="col-md-6">
            {this.props.description}<span id="rub">B</span>
          </div>
          <div className="col-md-6">
            <img className="img-responsive img-border-left" src={'img/products/'+this.props.image} alt={this.props.name} />
          </div>
          <div className="clearfix" />
        </div>
      </div>
    );
  }
});


ReactDOM.render(
  <ProductList url="/api/boxes"/>,
  document.getElementById('products-list')
);