package c5
 
import jcsp.lang.*
import groovyJCSP.*
import groovyJCSP.plugAndPlay.* 
import c05.Controller

import c05.ScaledData   
  
def data = Channel.createOne2One()
def timedData = Channel.createOne2One()
def scaledData = Channel.createOne2One()
def oldScale = Channel.createOne2One()
def newScale = Channel.createOne2One()
def pause = Channel.createOne2One()

def network = [ new GNumbers ( outChannel: data.out() ),
                new GFixedDelay ( delay: 1000, 
                		          inChannel: data.in(), 
                		          outChannel: timedData.out() ),
                new Scale ( inChannel: timedData.in(),
                            outChannel: scaledData.out(),
                            factor: oldScale.out(),
                            suspend: pause.in(),
                            injector: newScale.in(),
                            scalingFactor: 2 ),
                new Controller ( testInterval: 11000, //7000 this exercise, 11000 original
                		         computeInterval: 3000, //700 this exercise, 3000 original
                                 factor: oldScale.in(),
                                 suspend: pause.out(),
                                 injector: newScale.out() ),
                new GPrint ( inChannel: scaledData.in(),
                		     heading: "Original      Scaled",
                		     delay: 0)
              ]
new PAR ( network ).run()                                                            