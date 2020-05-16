# Create a Storage Account

To create a storage account please follow the [instructions in the documentation]( https://docs.microsoft.com/de-de/azure/storage/blobs/storage-upload-process-images?tabs=nodejsv10).

You can use this sample to create a basic account:
```bash
az storage account create --name <your-account-name> --location  westeurope  --resource-group  HelfenKannJeder --sku Standard_LRS --kind StorageV2 --access-tier hot
```

Afterwards you can get the connection string for the property `azure.storage.connection-string` as folow:
```
az storage account show-connection-string --name <your-account-name>
```