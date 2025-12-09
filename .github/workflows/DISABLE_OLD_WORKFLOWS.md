# Individual Service Workflows Disabled

**Date**: December 9, 2025  
**Reason**: All service-specific workflows were causing cascading build failures

## Problem

Each service had individual workflows that ran `mvn package --file pom.xml` from root.
This built ALL modules including notification-service, causing failures across all pipelines.

## Solution

All builds now use `.github/workflows/master-build.yml` which properly handles:
- Multi-module Maven builds
- Dependency resolution
- Test execution
- Artifact generation

## To Re-enable Individual Workflows

1. Rename files from `.yml.disabled` back to `.yml`
2. Update the `Build with Maven` step to:
   ```yaml
   - name: Build with Maven
     run: |
       mvn clean install -DskipTests
       mvn -B package -pl <module-name> -am
   ```

Example for order-service:
```yaml
- name: Build with Maven  
  run: mvn -B package -pl order-service -am
```

This builds ONLY order-service and its dependencies, not all modules.
