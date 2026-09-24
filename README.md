# Spring Boot Google OAuth2 Login

A small Spring Boot application that protects `/hello`, `/hi`, and `/hey` with Google OAuth2 login.

## Prerequisites

- Java 21
- A Google Cloud project with an **OAuth 2.0 Client ID** of type **Web application**

## Google Cloud Console setup

In **Google Cloud Console → APIs & Services → Credentials → your OAuth 2.0 Client ID**, add this value under **Authorized redirect URIs**:

```text
http://localhost:8080/login/oauth2/code/google
```

This is the callback URI used by Spring Security. Do not open it in the browser yourself; Google redirects to it after authentication.

If the consent screen is in testing mode, also add the Google account you use for testing under **OAuth consent screen → Test users**.

## Configuration

Do not commit a client secret. Provide credentials through environment variables instead:

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: ${GOOGLE_CLIENT_ID}
            client-secret: ${GOOGLE_CLIENT_SECRET}
            authorization-grant-type: authorization_code
            scope:
              - profile
              - email
```

`authorization_code` must use an underscore. `authorization-code` causes this error:

```text
Invalid Authorization Grant Type (authorization-code) for Client Registration with Id: google
```

You may omit `authorization-grant-type` because it is the default for the Google login flow.

Google is a built-in Spring Security provider, so the `provider.google` section is optional. If kept, it must have correct Google endpoint values.

## Run the application

macOS/Linux:

```bash
export GOOGLE_CLIENT_ID='your-client-id'
export GOOGLE_CLIENT_SECRET='your-client-secret'
./mvnw spring-boot:run
```

Windows PowerShell:

```powershell
$env:GOOGLE_CLIENT_ID='your-client-id'
$env:GOOGLE_CLIENT_SECRET='your-client-secret'
.\mvnw.cmd spring-boot:run
```

## Test login

1. Open `http://localhost:8080/hello`.
2. Spring Security redirects an unauthenticated user to `/login`.
3. Select **Google**, or open `http://localhost:8080/oauth2/authorization/google` directly.
4. Sign in to Google and approve access if prompted.
5. Google redirects to Spring Security's callback, and the application returns `hello`.

## Redirect straight to Google (optional)

By default, Spring Security first shows its `/login` page. To start the Google flow immediately for protected pages, use:

```java
.oauth2Login(oauth2 -> oauth2.loginPage("/oauth2/authorization/google"));
```

instead of:

```java
.oauth2Login(Customizer.withDefaults());
```

## Troubleshooting

| Symptom | Cause and fix |
| --- | --- |
| Redirect to `/login` | Normal default Spring Security behavior. Click Google or open `/oauth2/authorization/google`. |
| `Invalid Authorization Grant Type (authorization-code)` | Change it to `authorization_code`, or remove the setting. Restart the application. |
| `redirect_uri_mismatch` from Google | The Google Cloud Console redirect URI must exactly equal `http://localhost:8080/login/oauth2/code/google`. |
| Consent screen does not appear | Google skips consent when the current account already granted access. Test in an incognito window or remove this app from the account's third-party access. |
| App is blocked or unavailable to your Google account | Add the account as a test user while the OAuth consent screen is in testing mode. |

## Security note

If a client secret was committed, pasted, or shared, revoke/rotate it in Google Cloud Console immediately. Keep secrets in environment variables or a secrets manager.
