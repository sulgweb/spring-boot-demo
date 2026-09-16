package com.baize.sb.security;

import java.util.UUID;

public record AuthenticatedUser(UUID id, String name) {
}
