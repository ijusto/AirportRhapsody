#!/bin/bash

echo "Compiling Server Side"

#Interfaces

javac serverSide/interfaces/ArrivalLoungeInterface.java
javac serverSide/interfaces/ArrivalTerminalExitInterface.java
javac serverSide/interfaces/ArrivalTermTransfQuayInterface.java
javac serverSide/interfaces/BaggageColPointInterface.java
javac serverSide/interfaces/BaggageReclaimOfficeInterface.java
javac serverSide/interfaces/DepartureTerminalEntranceInterface.java
javac serverSide/interfaces/DepartureTermTransfQuayInterface.java
javac serverSide/interfaces/GenReposInfoInterface.java
javac serverSide/interfaces/TemporaryStorageAreaInterface.java

#Proxies

javac serverSide/proxies/ArrivalLoungeProxy.java
javac serverSide/proxies/ArrivalTerminalExitProxy.java
javac serverSide/proxies/ArrivalTermTransfQuayProxy.java
javac serverSide/proxies/BaggageColPointProxy.java
javac serverSide/proxies/BaggageReclaimOfficeProxy.java
javac serverSide/proxies/DepartureTerminalEntranceProxy.java
javac serverSide/proxies/DepartureTermTransfQuayProxy.java
javac serverSide/proxies/GenReposInfoProxy.java
javac serverSide/proxies/TemporaryStorageAreaProxy.java

#Servers

javac serverSide/servers/ServerArrivalLounge.java
javac serverSide/servers/ServerArrivalTerminalExit.java
javac serverSide/servers/ServerArrivalTermTransfQuay.java
javac serverSide/servers/ServerBaggageColPoint.java
javac serverSide/servers/ServerBaggageReclaimOffice.java
javac serverSide/servers/ServerDepartureTerminalEntrance.java
javac serverSide/servers/ServerDepartureTermTransfQuay.java
javac serverSide/servers/ServerGenReposInfo.java
javac serverSide/servers/ServerTemporaryStorageArea.java

#Shared Regions

javac serverSide/sharedRegions/ArrivalLounge.java
javac serverSide/sharedRegions/ArrivalTerminalExit.java
javac serverSide/sharedRegions/ArrivalTermTransfQuay.java
javac serverSide/sharedRegions/BaggageColPoint.java
javac serverSide/sharedRegions/BaggageReclaimOffice.java
javac serverSide/sharedRegions/DepartureTerminalEntrance.java
javac serverSide/sharedRegions/DepartureTermTransfQuay.java
javac serverSide/sharedRegions/GenReposInfo.java
javac serverSide/sharedRegions/TemporaryStorageArea.java

#ServerCom

javac serverSide/ServerCom.java

echo "Compiling Client Side"

#Clients

javac clientSide/clients/ClientBusDriver.java
javac clientSide/clients/ClientPassenger.java
javac clientSide/clients/ClientPorter.java

#Entities

javac clientSide/entities/BusDriver.java
javac clientSide/entities/BusDriverStates.java
javac clientSide/entities/Porter.java
javac clientSide/entities/PorterStates.java
javac clientSide/entities/Passenger.java
javac clientSide/entities/PassengerStates.java


#SharedRegionStubs

javac clientSide/sharedRegionsStubs/ArrivalLoungeStub.java
javac clientSide/sharedRegionsStubs/ArrivalTerminalExitStub.java
javac clientSide/sharedRegionsStubs/ArrivalTermTransfQuayStub.java
javac clientSide/sharedRegionsStubs/BaggageColPointStub.java
javac clientSide/sharedRegionsStubs/BaggageReclaimOfficeStub.java
javac clientSide/sharedRegionsStubs/DepartureTerminalEntranceStub.java
javac clientSide/sharedRegionsStubs/DepartureTermTransfQuayStub.java
javac clientSide/sharedRegionsStubs/GenReposInfoStub.java
javac clientSide/sharedRegionsStubs/TemporaryStorageAreaStub.java

#ClientCom
javac clientSide/ClientCom.java