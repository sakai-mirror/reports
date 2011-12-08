Included is a file reportsUpgrade.sql.  This is included for users running Oracle.  It will modify two of the Reports tables.  

First it will extend the ID from 36 to 255 characters.
Second, it will drop two Clobs and turn them into Blobs.

If you have data in your DB then you will want to make sure you back that up first before running this.
