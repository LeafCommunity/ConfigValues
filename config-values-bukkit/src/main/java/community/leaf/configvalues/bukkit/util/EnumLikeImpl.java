/*
 * Copyright © 2021-2025, RezzedUp <https://github.com/LeafCommunity/ConfigValues>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.configvalues.bukkit.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

final class EnumLikeImpl {
	
	static class Direct<E extends Enum<E>> implements EnumLike<E> {
		final Class<E> type;
		
		Direct(Class<E> type) {
			this.type = type;
		}
		
		@Override
		public Optional<E> valueOf(String name) {
			try {
				return Optional.of(Enum.valueOf(type, name));
			}
			catch (IllegalArgumentException ignored) {
				return Optional.empty();
			}
		}
		
		@Override
		public int ordinal(E thing) {
			return thing.ordinal();
		}
		
		@Override
		public String name(E thing) {
			return thing.name();
		}
		
		@Override
		public boolean equals(Object o) {
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			
			Direct<?> direct = (Direct<?>) o;
			return type.equals(direct.type);
		}
		
		@Override
		public int hashCode() {
			return type.hashCode();
		}
	}
	
	static class Indirect<T> implements EnumLike<T> {
		private final Class<T> type;
		private final Method staticValueOfMethod;
		private final Method instanceOrdinalMethod;
		private final Method instanceNameMethod;
		
		Indirect(Class<T> type) {
			this.type = Objects.requireNonNull(type, "type");
			
			try {
				this.staticValueOfMethod = type.getMethod("valueOf", String.class);
				if (!(type.isAssignableFrom(staticValueOfMethod.getReturnType())) && staticValueOfMethod.canAccess(null)) {
					throw new IllegalArgumentException(
						type.getCanonicalName() + " is not Enum-like: missing accessible static `T valueOf(String)` method"
					);
				}
				
				this.instanceOrdinalMethod = type.getMethod("ordinal");
				if (!int.class.isAssignableFrom(instanceOrdinalMethod.getReturnType())) {
					throw new IllegalArgumentException(
						type.getCanonicalName() + " is not Enum-like: missing accessible `int ordinal()` method"
					);
				}
				
				this.instanceNameMethod = type.getMethod("name");
				if (!String.class.isAssignableFrom(instanceNameMethod.getReturnType())) {
					throw new IllegalArgumentException(
						type.getCanonicalName() + " is not Enum-like: missing accessible `String name()` method"
					);
				}
			}
			catch (NoSuchMethodException e) {
				throw new RuntimeException(
					type.getCanonicalName() + " is not Enum-like: missing required method",
					e
				);
			}
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public Optional<T> valueOf(String name) {
			try {
				return Optional.ofNullable((T) staticValueOfMethod.invoke(null, name));
			}
			catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			catch (InvocationTargetException e) {
				if (e.getTargetException() instanceof IllegalArgumentException) {
					return Optional.empty();
				}
				throw new RuntimeException(e);
			}
		}
		
		@Override
		public int ordinal(T thing) {
			try {
				return (int) instanceOrdinalMethod.invoke(thing);
			}
			catch (InvocationTargetException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		
		@Override
		public String name(T thing) {
			try {
				return (String) instanceNameMethod.invoke(thing);
			}
			catch (InvocationTargetException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		
		@Override
		public boolean equals(Object o) {
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			
			Indirect<?> indirect = (Indirect<?>) o;
			return type.equals(indirect.type) && staticValueOfMethod.equals(indirect.staticValueOfMethod) && instanceOrdinalMethod.equals(indirect.instanceOrdinalMethod) && instanceNameMethod.equals(indirect.instanceNameMethod);
		}
		
		@Override
		public int hashCode() {
			int result = type.hashCode();
			result = 31 * result + staticValueOfMethod.hashCode();
			result = 31 * result + instanceOrdinalMethod.hashCode();
			result = 31 * result + instanceNameMethod.hashCode();
			return result;
		}
	}
}
