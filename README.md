## Introduction

LAM is Liferay Automated Migrations, allowing for statement-based configuration across environments.
Initially it's targeted towards development teams, but it's also definitely useful for functional/application administrators. 

## Features

* Human readable syntax (Groovy DSL)
* Synthesized/combined API on top of Liferay's APIs (hiding clunky Expando, DDM*, addUser(44 args), etc...)
* Exception handling
* Sensible defaults (no need to fiddle with companyIds and groupIds if you've only got one site)
* Statements groupable / orderable in separate files

## Usage

1. Create a module within your project, give it a name you like, for example `com.acme.myproject-config`
2. Create a class like this:
    ````
    @Component(immediate = true)
    public class SampleProjectConfig extends ProjectConfig {
    
        @Activate
        public void activate(BundleContext context) {
            super.doActivate(context);
        }
    
        @Reference
        protected void setExecutor(Executor executor) {
            this.executor = executor;
        }
    }	
    ````
3. Create configuration scripts in src/main/resources, named `*.groovy`, using [the syntax](documentation.md) 
4. Deploy to Liferay runtime

See also the module `nl.finalist.liferay.lam.sampleproject-config`, our flagship example config


## Roadmap

* Versioning (state management for every environment, keeping track of scripts already executed)
* Expose more APIs
* Control panel portlets for:
    - one-off script execution
    - status display


## Getting started with development

1. Clone this repo
2. download eclipse neon with liferay plugin. 
		Eg:
		https://sourceforge.net/projects/lportal/files/Liferay%20IDE/3.1.0%20B1/
3. Import existing Gradle project, point to base directory of cloned Git repo
4. Run 'gradle build'
5. Run unit tests
6. Deploy modules to Liferay to see it in action: `dslglue`, `api`, and `sampleproject-config`
