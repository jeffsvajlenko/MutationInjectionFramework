/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * DbUtils is a small set of classes designed to make working with JDBC  easier. JDBC resource cleanup code is mundane,
 * error prone work so these classes abstract out all of the cleanup tasks from your code leaving you with what you
 * really wanted to do with JDBC in the first place: query and update data.
 *
 * This package contains the core classes and interfaces - DbUtils, QueryRunner and the ResultSetHandler interface
 * should be your first items of interest.
 */
package org.apache.commons.dbutils;
