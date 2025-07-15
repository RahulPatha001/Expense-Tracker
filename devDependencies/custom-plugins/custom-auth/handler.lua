local kong = kong
local http = require "resty.http"
local cjson = require "cjson.safe"

local CustomAuthHandler = {
  PRIORITY = 1000,
  VERSION = "1.0",
}

function CustomAuthHandler:access(config)
  local auth_service_url = config.auth_service_url

  local token = kong.request.get_header("Authorization")
  kong.log.debug("Received Authorization: ", token)

  local httpc = http.new()
  httpc:set_timeouts(10000, 10000, 10000)

  local res, err = httpc:request_uri(auth_service_url, {
    method = "GET",
    headers = {
      ["Authorization"] = token
    }
  })

  if not res then
    kong.log.err("Failed to call auth service: ", err)
    return kong.response.exit(500, { message = "Internal Server Error" })
  end

  kong.log.debug("Auth service responded with status: ", res.status)
  kong.log.debug("Auth service raw response body: ", res.body)

  if res.status ~= 200 then
    kong.log.warn("Auth service responded with non-200: ", res.status)
    return kong.response.exit(res.status, { message = "Unauthorized" })
  end

  local user_id = nil
  local body, decode_err = cjson.decode(res.body)

  if body and type(body) == "table" and body.userId then
    user_id = body.userId
    kong.log.debug("Parsed userId from JSON: ", user_id)
  elseif res.body then
    -- Fallback: maybe body is plain string (e.g., "abc123")
    user_id = res.body:gsub('"', ''):gsub('%s+', '') -- strip quotes/whitespace
    kong.log.debug("Using raw body as userId fallback: ", user_id)
  end

  if not user_id or user_id == "" then
    kong.log.err("Failed to extract userId from auth response")
    return kong.response.exit(401, { message = "Unauthorized - Invalid Auth Response" })
  end

  kong.service.request.set_header("X-User-Id", user_id)
end

return CustomAuthHandler
