-- Add capacity and optimistic lock columns for enrollment concurrency control

ALTER TABLE course
  ADD COLUMN IF NOT EXISTS max_students INT DEFAULT 60,
  ADD COLUMN IF NOT EXISTS current_students INT DEFAULT 0,
  ADD COLUMN IF NOT EXISTS version INT DEFAULT 0;

UPDATE course c
SET c.current_students = (
  SELECT COUNT(*)
  FROM courses_students cs
  WHERE cs.course_id = c.course_id
)
WHERE c.current_students IS NULL OR c.current_students = 0;
