Fuse (on Spring Boot) to create a prototype to integrate JPA (with external DB), JMA (AMQ Broker), Drools (remote invocation), Rest APIs, Swagger.
# Install 
1. install [OKD](https://www.okd.io/) **3.11** following [these instructions](https://github.com/openshift/origin/blob/v3.11.0/docs/cluster_up_down.md)
1. `oc cluster up`
1. `oc login -u system:admin`
1. `oc project openshift`
    (from https://access.redhat.com/documentation/en-us/red_hat_decision_manager/7.3/html-single/deploying_a_red_hat_decision_manager_authoring_or_managed_server_environment_on_red_hat_openshift_container_platform/index#dm-openshift-prepare-con and https://access.redhat.com/RegistryAuthentication#registry-service-accounts-for-shared-environments-4)
1. `oc create -f <secret>.yaml`
1. `oc secrets link default <secret> --for=pull`
1. `oc secrets link builder <secret> --for=pull`
   (https://access.redhat.com/jbossnetwork/restricted/listSoftware.html?downloadType=distributions&product=rhdm&productChanged=yes)
1. `oc create -f ./Downloads/rhdm-7.3-openshift-templates/rhdm73-image-streams.yaml`
1. `oc import-image rhdm73-decisioncentral-openshift:1.0`
1. `oc import-image rhdm73-kieserver-openshift:1.0`
   (https://access.redhat.com/documentation/en-us/red_hat_amq/7.3/html-single/deploying_amq_broker_on_openshift_container_platform/index#installing-broker-ocp_broker-ocp)
1. `oc replace --force  -f https://raw.githubusercontent.com/jboss-container-images/jboss-amq-7-broker-openshift-image/73-7.3.0.GA/amq-broker-7-image-streams.yaml`
1. `oc import-image amq-broker-7/amq-broker-73-openshift --from=registry.redhat.io/amq-broker-7/amq-broker-73-openshift --confirm`
1. `for template in amq-broker-73-basic.yaml amq-broker-73-ssl.yaml amq-broker-73-custom.yaml amq-broker-73-persistence.yaml amq-broker-73-persistence-ssl.yaml amq-broker-73-persistence-clustered.yaml amq-broker-73-persistence-clustered-ssl.yaml;  do  oc replace --force -f https://raw.githubusercontent.com/jboss-container-images/jboss-amq-7-broker-openshift-image/73-7.3.0.GA/templates/${template};  done`
   (https://access.redhat.com/documentation/en-us/red_hat_fuse/7.3/html-single/fuse_on_openshift_guide/index#get-started-admin-install)
1. `BASEURL=https://raw.githubusercontent.com/jboss-fuse/application-templates/application-templates-2.1.fuse-730065-redhat-00002`
1. `oc create -n openshift -f ${BASEURL}/fis-image-streams.json`
1. `for template in eap-camel-amq-template.json  eap-camel-cdi-template.json  eap-camel-cxf-jaxrs-template.json  eap-camel-cxf-jaxws-template.json  eap-camel-jpa-template.json  karaf-camel-amq-template.json  karaf-camel-log-template.json  karaf-camel-rest-sql-template.json  karaf-cxf-rest-template.json  spring-boot-camel-amq-template.json  spring-boot-camel-config-template.json  spring-boot-camel-drools-template.json  spring-boot-camel-infinispan-template.json  spring-boot-camel-rest-sql-template.json  spring-boot-camel-teiid-template.json  spring-boot-camel-template.json  spring-boot-camel-xa-template.json  spring-boot-camel-xml-template.json  spring-boot-cxf-jaxrs-template.json  spring-boot-cxf-jaxws-template.json ;  do  oc create -n openshift -f  https://raw.githubusercontent.com/jboss-fuse/application-templates/application-templates-2.1.fuse-730065-redhat-00002/quickstarts/${template};  done`
1. `oc login -u developer`
1. `oc new-project migration-analytics`
1. `oc create -f <secret>.yaml`
1. `oc secrets link default <secret> --for=pull`
1. `oc secrets link builder <secret> --for=pull`
#Deploy
## OCP templates
1. `for template in eap7-app-secret.json mysql-secret.json analytics_template.json;  do  oc process -f https://raw.githubusercontent.com/mrizzi/sample-analytics-integration/master/src/main/resources/okd/${template}| oc create -f -;  done`
## Decision Manager
1. go to Application -> Routes page and click on the URL in the `Hostname` column beside the `myapp-rhdmcentr` service
1. login with `adminUser`-`password`
1. click on `Design` link
1. click on `Import Project` button
1. in the `Repository URL` field paste `https://github.com/mrizzi/sample-analytics.git`
1. select `sample-analytics` box and click the `OK` button on the upper right side
1. once the import has finished, click the `Build & Install` button from the upper right `Build` menu
1. once the build has been successfully done, click on the `Deploy` button
#Manage
## MySql
1. from pod's `Terminal` tab execute `mysql --user userAJL --password sampledb` (pwd: `oRk1CWV21JttTtxP`) 
1. `select * from report_data_model;`
## AMQ Broker
AMQ Web Console
## Camel routes
# Undeploy
1. `oc delete all,pvc -l application=migration-analytics -n migration-analytics`
## References