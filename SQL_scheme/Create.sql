CREATE TABLE "public.courses" (
	"id" serial NOT NULL,
	"title" varchar NOT NULL,
	"author" varchar NOT NULL,
	"description" TEXT NOT NULL,
	"creation_date" DATE NOT NULL,
	"creation_author_id" bigint NOT NULL,
	"duration_hours" int NOT NULL,
	"edit_date" DATE NOT NULL,
	"edit_author_id" bigint NOT NULL,
	"delete_date" DATE,
	"delete_author" varchar,
	"module_id" serial NOT NULL UNIQUE,
	"tag" varchar NOT NULL,
	"category_id" bigint NOT NULL,
	"deleted" serial NOT NULL,
	CONSTRAINT "courses_pk" PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "public.users" (
	"id" serial NOT NULL,
	"e-mail" varchar NOT NULL,
	"nickname" varchar NOT NULL,
	"password" varchar NOT NULL,
	"avatar" varchar,
	"first_name" varchar NOT NULL,
	"last_name" varchar NOT NULL,
	"registration_date" DATE NOT NULL,
	"info_update_date" DATE NOT NULL,
	"info_update_author_id" bigint NOT NULL,
	"delete_date" DATE,
	"delete_author_id" bigint,
	"administrator" BOOLEAN NOT NULL,
	"deleted" BOOLEAN NOT NULL,
	CONSTRAINT "users_pk" PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "public.modules" (
	"id" serial NOT NULL,
	"title" varchar NOT NULL,
	"description" TEXT NOT NULL,
	"creation_date" DATE NOT NULL,
	"creation_author_id" bigint NOT NULL,
	"edit_date" DATE NOT NULL,
	"edit_author_id" bigint NOT NULL,
	"delete_date" DATE,
	"delete_author_id" bigint,
	"theme_id" varchar NOT NULL,
	"deleted" BOOLEAN NOT NULL,
	CONSTRAINT "modules_pk" PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "public.users-courses" (
	"course_id" bigint NOT NULL,
	"user_id" bigint NOT NULL
) WITH (
  OIDS=FALSE
);



CREATE TABLE "public.themes" (
	"id" serial NOT NULL,
	"title" varchar NOT NULL,
	"description" TEXT NOT NULL,
	"creation_date" DATE NOT NULL,
	"creation_author_id" bigint NOT NULL,
	"edit_date" DATE NOT NULL,
	"edit_author_id" bigint NOT NULL,
	"delete_date" DATE,
	"delete_author_id" bigint,
	"content_id" bigint NOT NULL,
	"deleted" BOOLEAN NOT NULL,
	CONSTRAINT "themes_pk" PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "public.rating" (
	"course_id" bigint NOT NULL,
	"user_id" bigint NOT NULL,
	"score" float4 NOT NULL
) WITH (
  OIDS=FALSE
);



CREATE TABLE "public.categories" (
	"id" serial NOT NULL,
	"title" varchar NOT NULL,
	CONSTRAINT "categories_pk" PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "public.content" (
	"id" serial NOT NULL,
	"text" TEXT,
	"audio" varchar,
	"video" varchar,
	"attachment" varchar,
	CONSTRAINT "content_pk" PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



ALTER TABLE "courses" ADD CONSTRAINT "courses_fk0" FOREIGN KEY ("creation_author_id") REFERENCES "users"("id");
ALTER TABLE "courses" ADD CONSTRAINT "courses_fk1" FOREIGN KEY ("edit_author_id") REFERENCES "users"("id");
ALTER TABLE "courses" ADD CONSTRAINT "courses_fk2" FOREIGN KEY ("module_id") REFERENCES "modules"("id");
ALTER TABLE "courses" ADD CONSTRAINT "courses_fk3" FOREIGN KEY ("category_id") REFERENCES "categories"("id");

ALTER TABLE "users" ADD CONSTRAINT "users_fk0" FOREIGN KEY ("info_update_author_id") REFERENCES "users"("id");
ALTER TABLE "users" ADD CONSTRAINT "users_fk1" FOREIGN KEY ("delete_author_id") REFERENCES "users"("id");

ALTER TABLE "modules" ADD CONSTRAINT "modules_fk0" FOREIGN KEY ("creation_author_id") REFERENCES "users"("id");
ALTER TABLE "modules" ADD CONSTRAINT "modules_fk1" FOREIGN KEY ("edit_author_id") REFERENCES "users"("id");
ALTER TABLE "modules" ADD CONSTRAINT "modules_fk2" FOREIGN KEY ("delete_author_id") REFERENCES "users"("id");
ALTER TABLE "modules" ADD CONSTRAINT "modules_fk3" FOREIGN KEY ("theme_id") REFERENCES "themes"("id");

ALTER TABLE "users-courses" ADD CONSTRAINT "users-courses_fk0" FOREIGN KEY ("course_id") REFERENCES "courses"("id");
ALTER TABLE "users-courses" ADD CONSTRAINT "users-courses_fk1" FOREIGN KEY ("user_id") REFERENCES "users"("id");

ALTER TABLE "themes" ADD CONSTRAINT "themes_fk0" FOREIGN KEY ("creation_author_id") REFERENCES "users"("id");
ALTER TABLE "themes" ADD CONSTRAINT "themes_fk1" FOREIGN KEY ("edit_author_id") REFERENCES "users"("id");
ALTER TABLE "themes" ADD CONSTRAINT "themes_fk2" FOREIGN KEY ("delete_author_id") REFERENCES "users"("id");
ALTER TABLE "themes" ADD CONSTRAINT "themes_fk3" FOREIGN KEY ("content_id") REFERENCES "content"("id");

ALTER TABLE "rating" ADD CONSTRAINT "rating_fk0" FOREIGN KEY ("course_id") REFERENCES "courses"("id");
ALTER TABLE "rating" ADD CONSTRAINT "rating_fk1" FOREIGN KEY ("user_id") REFERENCES "users"("id");











