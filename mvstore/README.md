## Get h2-mvstore-1.4.200.jar

1.  clone https://github.com/h2database/h2database.git

2.  run shell 
    ```````
    ./build.sh jarMVStore
    
    ```````
    
3.  edit build.gradle(line 130) file in this module, fill in your actual h2-mvstore jar path.

4.  run gradle task 'proguard' ,and you can also use the 'publish' task to publish jar 


## Note

1.  check you java version, and 1.8 is best.