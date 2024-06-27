# V1

## Pre coaching

### Geschatte Progress (in procent): 30%

### Status

_Waar sta je globaal? Wat loopt goed en minder goed? Hoe verloopt de samenwerking? Wie heeft globaal welke delen van de applicatie uitgewerkt? (details progress kunnen we bekijken op het issue board)_

Door ieders drukke agenda is het niet evident om vaak samen te komen. We hebben de taken zo verdeeld dat ieders één van de drie applicaties onder handen neemt.
Thomas focust momenteel op bakery, Andreas op warehouse en Jimmy op clients. 
Clients zijn de stories die betrekking hebben op logica en endpoints al naar behoren uitgewerkt, maar dient er nog wel wat gerefactord te worden.
Hier moet ook nog wel werk gemaakt worden van logging, testing, security en queueing.
In clients wordt er momenteel nog steeds gewerkt met een lokaal opgezette db, wederom de issues met de docker image.
We hopen dat als we verder gaan met keycloak en queueing dat we niet hetzelfde probleem met de dockerfile gaan tegenkomen, anders moeten deze ook lokaal voorzien worden.
Voor de bakery is al een uitgebreide logica aanwezig, maar het vertalen naar een thymeleaf frontend (ipv de eerdere restcontroller) vergt tijd en moeite.
In Warehouse applicatie worden op dit moment nog zaken verbeterd op vlak van architectuur en wordt de integratie van asynchrone berichten geïmplementeerd.  
Basislogica in controllers en services werd geïmplementeerd voor user stories betreffende orderverwerking, communicatie met leveranciers en voorraadbeheer.

### Stories

_(enkel voor stories die speciale aandacht vergen)_
Clients : focus gaat komende periode liggen op de stories die betrekking hebben op 
testing, security (onderscheid tussen klant en klantenbeheerder bvb) en de nodige communicatie met de andere applicaties.
nakijken of bij annuleren de (indien we ze bij create toekennen) punten en kortingen ook geannuleerd worden!!!.
Warehouse: integratie van automatische bestellingen bij leveranciers zal gecombineerd moeten worden met de andere applicaties.
Nauwkeurigheid van stock updates.

### Quality

_Acties (refactorings,...) die nog gepland staan om de kwaliteit van je project te verhogen (maak hiervoor issue(s) aan!): [issue nummer]: toelichting_

Clients: wederom voornamelijk focus op testing, security en logging.
Bakery: testing(!), security, logging + implementatie andere functionaliteit (endpoints van obsolete restcontroller verwerken in thymeleaf frontend: voornamelijk update + delete);
Warehouse: error handling, unit and integration testing
        
### Vragen

_eventuele vragen voor je coach_

Clients: standaardvraag is natuurlijk de dockerfile, maar dit lossen we momenteel op door db lokaal te hosten via postgres admin
    - bestellingen die de klant maakt zijn aan hand van UUID, gezien we met REST api's werken is dit voldoende?
    - we kunnen een bestelling aanmaken, confirmeren en annuleren, moet er ook een mogelijkheid tot wijzigen voorzien worden?
    - timing van toekennen punten + korting, bij aanmaken of bij confirmeren? onderscheid tussen kans op "fraude" <=> meerdere bestellingen, nog niet geconfirmeerd dus geen kortingen
    - aansluitend, moeten deze gereturned worden bij aanmaken van een order?
    - story 15 => toestand purchase order, hoe moeten we dit zien. Order kan momenteel op created, confirmed of cancelled staan (in overzicht ontvangen, in verwerking, verwerkt, foutief).
    - story 16 => zien we ongeprijsde producten enkel met null of ook met "0"?
    - aansluitend story 18 wat verstaan we onder  blanco prijs (momenteel zo opgebouwd dat artikel met prijs null op non actief komt te staan en pas na toekenning waarde op actief), wat bij bestelling product dat op non actief staat
    - 53 => momenteel algemeen overzicht mogelijk, hoever uitbreiden?


## Post coaching

### Feedback

-transactional annotatie in service layer
-bad request in controller veranderen door exception, zelf exceptions maken en afhandelen
-try catch vervangen, controller advice van spring?? even uitzoeken wat hiermee bedoelt
-dto mapping in controller(reeds aan begonnen)

vragen:
    - punten toekennen bij confirm, prijs + korting bij create, bij confirm loyalty punten toekennen
    - get batch status gaat niet over order status, wel over verwerking van het batch order zelf
    - null voor ongeprijsde producten
    - 1 product niet actief gehele order annuleren
    - 53 mag basic blijven

# V2

## Pre coaching

### Geschatte Progress (in procent): XX%

### Status

_Waar sta je globaal? Wat loopt goed en minder goed? Hoe verloopt de samenwerking? Wie heeft globaal welke delen van de applicatie uitgewerkt? (details progress kunnen we bekijken op het issue board)_

### Stories

_(enkel voor stories die speciale aandacht vergen)_

### Quality

_Acties (refactorings,...) die nog gepland staan om de kwaliteit van je project te verhogen (maak hiervoor issue(s) aan!): [issue nummer]: toelichting_

### Vragen

_eventuele vragen voor je coach_

## Post coaching

### Feedback

_(in te vullen na gesprek)_