package net.osgg.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class RestVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
	  Router router = Router.router(vertx);
      router.get("/api/v1/convert/:value").handler(this::getFahrenheit);
      router.get("/api/v1/greeting/:name").handler(this::getName);
      router.get("/api/v1/biciesto/:anio").handler(this::getBiciesto);
      
	    vertx.createHttpServer()
	      .requestHandler(router)
	      .listen(8888)
	      .onSuccess(server ->
	        System.out.println(
	          "HTTP server started on port " + server.actualPort()
	        )
	      );
	  }
  
  private void getBiciesto(RoutingContext routingContext) {
      String value = routingContext.request().getParam("anio");
      float anio = Float.valueOf(value);
      int esBiciesto = 0;
      
      if(anio % 4 == 0)
    	  if(anio % 100 == 0)
    		  if(anio % 400 == 0)
    			  esBiciesto = 1;
    		  else
    			  esBiciesto = 0;
    	  else
    		  esBiciesto = 1;
      else
    	  esBiciesto = 0;
      
      String respuesta = "";
      if(esBiciesto == 1)
    	  respuesta = value + " es biciesto";
      else
    	  respuesta = value + " no es biciesto";

      Result r = new Result(String.valueOf(respuesta));
      routingContext.response()
              .putHeader("content-type", "application/json")
              .setStatusCode(200)
              .end(Json.encodePrettily(r));
  }
  
  private void getFahrenheit(RoutingContext routingContext) {
      String value = routingContext.request().getParam("value");
      float celsius = Float.valueOf(value);
      float f =  (celsius * 9 / 5) + 32;
      Result r = new Result(String.valueOf(f));
      routingContext.response()
              .putHeader("content-type", "application/json")
              .setStatusCode(200)
              .end(Json.encodePrettily(r));
  }
  
  
  private void getName(RoutingContext routingContext) {
      String value = routingContext.request().getParam("name");
      Result r = new Result("Hello "+value+"!");
      routingContext.response()
              .putHeader("content-type", "application/json")
              .setStatusCode(200)
              .end(Json.encodePrettily(r));
  }
  
}

