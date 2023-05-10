# Kino
Übungsaufgabe für die Schule

Projekt – Kino

Datenbankstruktur
SQLite Datenbank
Tabellen:
1.	User
	a.	ID
	b.	Username
	c.	Vorname
	d.	Nachname
	e.	Email
	f.	Passwort
	g.	BerechtigunsGruppenID FK
2.	Berechtigungsgruppe
	a.	ID
	b.	Name
	c.	Berechtigungsstufe
3.	Kinosaal
	a.	SaalNr
	b.	Name
	c.	AnzPlätze
4.	Seats
	a.	ID
	b.	SeatNr
	c.	SaalNr FK
	d.	Platzkategorie
5.	Buchung
	a.	ID
	b.	Username FK
6.	Reservierung
	a.	ID
	b.	Buchung FK
	c.	Vorstellung FK
	d.	SitzPlatzID FK
7.	Vorstellung
	a.	ID
	b.	Saal FK
	c.	Film FK
	d.	Startzeit
8.	Film
	a.	ID
	b.	Name
	c.	Länge
	d.	Preiskategorie
9.	PreisKategorie
	a.	ID
	b.	Name
	c.	Kategorie
	d. 	Preis
10.	I18N
	a.	Key
	b.	Value
11.	PreisModi
	a.	ID
	b.	Name
	c.	ModifierinEur
	d.	Beschreibung

GUI
Fenster:
1.	Login		mit Anzeigeprio
2.	Register	mit Anzeigeprio
3.	Main 
a.	Vorstellungen Aussuchen
i.	Ticket Buchung
4.	Stornieren/Verwalten von Buchungen
Rabatte
	Kinder
	Behindert
	Senior	

Preiskategorien
	Basic
	3D
	Überlänge
	Dolby Surround

Platzkategorien
	Basic
	D-Box
	(Bestimmte Positionen)
	Love-Seats

GITHub

Branches:
	Für jede Größere Anpassung neuen Featurebranch.

	
