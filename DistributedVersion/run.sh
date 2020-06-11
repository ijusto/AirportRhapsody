#!/bin/bash
echo "Starting Server and Clients"
#Servers
gnome-terminal -- java serverSide/servers/ServerArrivalLounge &
java serverSide/servers/ServerArrivalTerminalExit &
java serverSide/servers/ServerArrivalTermTransfQuay &
java serverSide/servers/ServerBaggageColPoint &
java serverSide/servers/ServerBaggageReclaimOffice &
java serverSide/servers/ServerDepartureTerminalEntrance &
java serverSide/servers/ServerDepartureTermTransfQuay &
java serverSide/servers/ServerGenReposInfo &
java serverSide/servers/ServerTemporaryStorageArea &

#Clients
gnome-terminal -- java clientSide/clients/ClientPassenger
gnome-terminal -- java clientSide/clients/ClientPorter
gnome-terminal -- java clientSide/clients/ClientBusDriver
