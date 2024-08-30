import { S3Client, PutObjectCommand } from '@aws-sdk/client-s3';
import { useEffect, useMemo, useRef } from 'react';
import ReactQuill from 'react-quill-new';
import 'react-quill-new/dist/quill.snow.css';
import './MyEditor.css';

import propTypes from 'prop-types';

const REGION = import.meta.env.REACT_APP_AWS_S3_BUCKET_REGION;
const ACCESS_KEY = import.meta.env.REACT_APP_AWS_S3_BUCKET_ACCESS_KEY_ID;
const SECRET_ACCESS_KEY = import.meta.env
  .REACT_APP_AWS_S3_BUCKET_SECRET_ACCESS_KEY;

function MyEditor({ funding, onChange, setRegistActive }) {
  const editorRef = useRef(null);

  useEffect(() => {
    const isEmptyContent =
      !funding.content || funding.content === '<p><br></p>';
    setRegistActive(!isEmptyContent);
    console.log(funding.content);
  }, [funding.content]);

  const handleChange = (value) => {
    // content 값이 변경될 때 onChange를 호출하여 funding 객체를 업데이트
    onChange({ target: { id: 'content', value } });
  };

  const handleImageUpload = (file, callback) => {
    const reader = new FileReader();
    reader.onload = (e) => {
      const base64 = e.target.result;
      callback(base64, '이미지 설명');
    };
    reader.readAsDataURL(file);
  };

  const imageHandler = async () => {
    const input = document.createElement('input');
    input.setAttribute('type', 'file');
    input.setAttribute('accept', 'image/*');
    input.click();
    input.addEventListener('change', async () => {
      // 이미지를 담아 전송할 file
      const file = input.files?.[0];
      try {
        // 생성한 s3 관련 설정들
        const s3Client = new S3Client({
          region: REGION,
          credentials: {
            accessKeyId: ACCESS_KEY,
            secretAccessKey: SECRET_ACCESS_KEY,
          },
        });

        const params = {
          Bucket: 'itsmovietime',
          Key: `upload/${Date.now()}`,
          Body: file,
          ACL: 'public-read',
        };

        const command = new PutObjectCommand(params);
        const { Location } = await s3Client.send(command);

        const editor = editorRef.current.getEditor();
        const range = editor.getSelection();
        editor.insertEmbed(range.index, 'image', Location);
      } catch (error) {
        console.error('Error uploading image: ', error);
      }
    });
  };

  const modules = useMemo(() => {
    return {
      toolbar: {
        container: [
          [{ header: '1' }, { header: '2' }],
          ['bold', 'underline'],
          ['image'],
          [{ list: 'ordered' }, { list: 'bullet' }],
          ['link'],
          [{ align: [] }],
        ],
        handlers: {
          image: imageHandler,
        },
      },
      clipboard: {
        matchVisual: false,
      },
    };
  }, []);

  return (
    <div className="react-quill-container">
      <ReactQuill
        ref={editorRef}
        value={funding.content || ''}
        onChange={handleChange}
        theme="snow"
        modules={modules}
        formats={[
          'header',
          'list',
          'bold',
          'italic',
          'underline',
          'link',
          'image',
          'align',
        ]}
        hooks={{
          addImageBlobHook: handleImageUpload,
        }}
        style={{ height: '350px' }}
      />
    </div>
  );
}

MyEditor.propTypes = {
  funding: propTypes.object.isRequired,
  onChange: propTypes.func.isRequired,
  setRegistActive: propTypes.func.isRequired,
};

export default MyEditor;
